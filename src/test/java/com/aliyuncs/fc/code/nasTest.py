import SimpleHTTPServer
import SocketServer
import socket
import json
import time
import random
import string
import threading
import requests
import oss2
import stat
import os.path
import shutil
from os import path

# This is the entrypoint of function.
def handler(event, context):
    print('Payload: {}'.format(event))
    evt  = json.loads(event)
    task = evt['task']
    tasks = {
        'test_nas': test_nas,
        'no_nas': no_nas,
        'write_nas': write_nas,
        'read_nas': read_nas,
        'delete_nas': delete_nas,
        'delete_dir': delete_dir,
        'oss_put': oss_put,
        'internet_access': internet_access,
        'sanity_check': sanity_check,
        'detect_ip': detect_ip,
        'detect_eni_ip': detect_eni_ip,
        'ping_ea': ping_ea,
        'ping_ea_from_minion': ping_ea_from_minion,
        'ping_server': ping_server,
        'ping_minion': ping_minion,
        'start_server': start_server,
    }
    output = tasks[task](evt)
    print('Task completed')
    return output

# Ping represent a ping object.
class Ping:
    def __init__(self):
        self.count = 0
    def increment(self):
        self.count += 1


# Write and read from nas with two mount points pointing to the same NFS volume
def test_nas(evt):
    try:
        root_dir1 = evt['mount_dir1']
        root_dir2 = evt['mount_dir2']
        user_id = evt['user_id']
        group_id = evt['group_id']
        sub_dir = randomString(16)
        if int(user_id) != os.geteuid():
            return False
        if int(group_id) != os.getgid():
            return False
        file_name = randomString(6)+'.txt'
        dir1 = root_dir1 + '/' + sub_dir + '/'
        dir2 = root_dir2 + '/' + sub_dir + '/'
        content = "NAS here I come"
        os.mkdir(dir1)
        fw = open(dir1+file_name, "w+")
        fw.write(content)
        fw.close()
        fr = open(dir2+file_name)
        line = fr.readline()
        if line != content:
            return False
        fr.close()
        os.remove(dir2+file_name)
        os.rmdir(dir2)
        return True
    except Exception as e:
        print('Exception: {}'.format(e))
        return str(e)

# Make sure we don't have nas
def no_nas(evt):
    try:
        mount_dir = evt['mount_dir']
        fr = os.chdir(mount_dir)
        return False
    except Exception as e:
        print('Exception: {}'.format(e))
        return True

# Write the specific content to the dir and file
def write_nas(evt):
    try:
        write_dir = evt['write_dir']
        file_name = evt['file_name']
        content = evt['content']
        user_id = evt['user_id']
        group_id = evt['group_id']
        if int(user_id) != os.geteuid():
            return False
        if int(group_id) != os.getgid():
            return False
        if not os.path.exists(write_dir):
            os.mkdir(write_dir)
        fw = open(write_dir+"/"+file_name, "w+")
        fw.write(content)
        fw.close()
        os.chmod(write_dir+"/"+file_name, stat.S_IRUSR |stat.S_IWUSR| stat.S_IRGRP)
        return True
    except Exception as e:
        print('Exception: {}'.format(e))
        return str(e)

# Read from the specific dir and file
def read_nas(evt):
    try:
        read_dir = evt['read_dir']
        file_name = evt['file_name']
        content = evt['content']
        user_id = evt['user_id']
        group_id = evt['group_id']
        gc = evt['gc']
        if int(user_id) != os.geteuid():
            return False
        if int(group_id) != os.getgid():
            return False
        fr = open(read_dir+"/"+file_name)
        txt = fr.read()
        if txt != content:
            return False
        fr.close()
        if gc == 'True':
            os.remove(read_dir+"/"+file_name)
            os.rmdir(read_dir)
        return True
    except Exception as e:
        print('Exception: {}'.format(e))
        return str(e)

# Delete from the specific dir and file
def delete_nas(evt):
    try:
        delete_dir = evt['delete_dir']
        file_name = evt['file_name']
        user_id = evt['user_id']
        group_id = evt['group_id']
        if int(user_id) != os.geteuid():
            return False
        if int(group_id) != os.getgid():
            return False
        os.remove(delete_dir+"/"+file_name)
        os.rmdir(delete_dir)
        return True
    except Exception as e:
        print('Exception: {}'.format(e))
        return str(e)

# Delete the specific dir even if it's not empty
def delete_dir(evt):
    try:
        delete_dir = evt['delete_dir']
        recursive = evt['recursive']
        if recursive == 'True':
            shutil.rmtree(delete_dir)
        else:
            os.rmdir(delete_dir)
        return True
    except Exception as e:
        print('Exception: {}'.format(e))
        return str(e)



# Put an object in oss. This is used for testing async invoke.
def oss_put(evt):
    bucket_name = evt['bucket_name']
    object_name = evt['object_name']
    oss_endpoint = evt['oss_endpoint']
    access_key = evt['access_key']
    secret_key = evt['secret_key']

    try:
        auth = oss2.Auth(access_key, secret_key)
        bucket = oss2.Bucket(auth, oss_endpoint, bucket_name)
        bucket.put_object(object_name, object_name)
        print('oss put: {}'.format(object_name))
        return 'done'
    except Exception as e:
        print('Exception: {}'.format(e))
        return e

# Return true indicates the network access ability meets the expectation. If expected
# status code is -1, we expect the call will fail with exception.
def internet_access(evt):
    url = evt['url']
    status_code = evt['status_code']
    timeout = evt['timeout']
    try:
        r = requests.get(url, timeout=timeout)
        if r.status_code == status_code:
            return True
        print('Unexpected response: {}, {}'.format(r.status_code, r.text))
        return False
    except Exception as e:
        print('Exception: {}'.format(e))
        if status_code == -1:
            return True
        return False

# Sanity check will run 'detect_ip', 'ping_ip' and 'run_local_http_server'
# all at once in a single container to test the functionality of these methods.
# Return true indicates everything works as expected.
def sanity_check(evt):
    evt['ip'] = detect_ip(evt)
    server_ping = Ping()
    client_ping = Ping()
    def server():
        server_ping.count = start_server(evt)
    def client():
        client_ping.count = ping_server(evt)

    t1 = threading.Thread(target=server, args=())
    t2 = threading.Thread(target=client, args=())
    t1.start()
    t2.start()
    t1.join()
    t2.join()
    return server_ping.count > 0 and server_ping.count == client_ping.count

# Return the IP address of the container.
def detect_ip(evt):
    return socket.gethostbyname(socket.gethostname())

# Return the attached ENI IP. The ENI is obtained from minion echo task.
def detect_eni_ip(evt):
    ip = evt['minion_ip']
    port = evt['minion_port']
    timeout = evt['timeout']

    try:
        r = requests.get('http://{}:{}/echo'.format(ip, port), timeout=timeout)
        if r.status_code == 200:
            data = json.loads(r.text)
            return data['SourceIP']
        print('Response is not expected: {}'.format(r.text))
        return r
    except Exception as e:
        print('Exception {}'.format(e))
        return e

# Ping the EA with given information. Return the number of successful ping count.
def ping_ea(evt):
    ip = evt['ip']
    port = evt['port']
    timeout = evt['timeout']
    ping = Ping()
    start_time = time.time()

    while time.time() - start_time <= timeout:
        try:
            # Something is really wrong if we can actually curl the address without getting any error.
            r = requests.get('http://{}:{}'.format(ip, port), timeout=timeout)
            print('Something is really wrong: {}'.format(r))
            ping.increment()
        except Exception as e:
            print('Exception {}'.format(e))
            msg = str(e)
            # Container in a docker network is prohibited to establish any connection with EA and any IP address
            # in the default namespace. This is enforced by iptables rules. Trying to establish a connection with
            # IP address in the default namespace should receive a "Failed to establish a new connection" error.
            #
            # EA is implemented with grpc protocol. Successfully curling the EA should receive the following error.
            # ('Connection aborted.', error(104, 'Connection reset by peer'))
            if 'Connection aborted' in msg and 'Connection reset by peer' in msg:
                ping.increment()

    print('EA ping {}'.format(ping.count))
    return ping.count

# Ping the EA from minion. Return the number of successful ping count.
def ping_ea_from_minion(evt):
    minion_ip = evt['minion_ip']
    minion_port = evt['minion_port']
    ea_ip = evt['ea_ip']
    ea_port = evt['ea_port']
    timeout = evt['timeout']

    try:
        r = requests.get('http://{}:{}/ping_ea?ip={}&port={}&timeout={}'.format(minion_ip, minion_port, ea_ip, ea_port, timeout))
        data = json.loads(r.text)
        ping = data['Ping']
        print('Minion pings EA {}'.format(ping))
        return ping
    except Exception as e:
        # This should never happen, we expect the minion request always returns a ping count.
        return e

# Ping the given IP in certain amount of time. Return the number of successful ping count.
def ping_server(evt):
    ip = evt['ip']
    port = evt['port']
    timeout = evt['timeout']
    msg = evt['message']
    ping = Ping()
    start_time = time.time()

    while time.time() - start_time <= timeout:
        try:
            r = requests.get('http://{}:{}'.format(ip, port), timeout=timeout)
            if r.status_code == 200 and r.text == msg:
                ping.increment()
            else:
                print('Response is not expected: {}'.format(r.text))
        except Exception as e:
            print(e)

    print('client ping {}'.format(ping.count))
    return ping.count

# Ping the minion. Expect the minion echoes back the request path. Return the successful ping count.
# Echo minion is defined in `test/utils/service/minion/tasks/vpc_network_suite`.
def ping_minion(evt):
    src_ip = evt['source_ip']
    check_src_ip = evt['check_source_ip']
    ip = evt['minion_ip']
    port = evt['minion_port']
    timeout = evt['timeout']
    ping = Ping()
    start_time = time.time()

    while time.time() - start_time <= timeout:
        try:
            path = '/echo?{}'.format(randomString(16))
            r = requests.get('http://{}:{}{}'.format(ip, port, path), timeout=timeout)
            if r.status_code == 200:
                data = json.loads(r.text)
                if data['Path'] == path:
                    if check_src_ip:
                        if data['SourceIP'] == src_ip:
                            ping.increment()
                    else:
                        ping.increment()
                else:
                    print('Path or source IP does not match: {}'.format(r.text))
            else:
                print('Response is not expected: {}'.format(r.text))
        except Exception as e:
            print(e)

    print('minion ping {}'.format(ping.count))
    return ping.count

# Run a local http server for certain amount of time. Return a ping count for
# the number of requests that the local http server recives.
def start_server(evt):
    port = evt['port']
    timeout = evt['timeout']
    msg = evt['message']
    ping = Ping()

    # This is a simple http request handler.
    class Handler(SimpleHTTPServer.SimpleHTTPRequestHandler):
        def do_GET(self):
            ping.increment()
            self.send_response(200)
            self.end_headers()
            self.wfile.write(msg)

    # Call shut down to shut down the running server.
    def shut_down(httpd, wait_time):
        time.sleep(wait_time)
        httpd.shutdown()

    # Start a local server.
    httpd = SocketServer.TCPServer(("", port), Handler)
    t = threading.Thread(target=shut_down, args=(httpd, timeout))
    t.start()
    print('start server at port {}'.format(port))
    httpd.serve_forever()
    t.join()
    print('shut down server')
    print('server ping {}'.format(ping.count))
    return ping.count

# Return a random string of length n.
def randomString(n):
    return ''.join(random.SystemRandom().choice(string.ascii_uppercase + string.digits) for _ in range(n))
