from adb_shell.auth.keygen import keygen
from adb_shell.adb_device import AdbDeviceTcp
from adb_shell.auth.sign_pythonrsa import PythonRSASigner
from pathlib import Path
import ctypes, inspect, threading, time

device = None
thread = None

def _async_raise(tid, exctype):
    tid = ctypes.c_long(tid)
    if not inspect.isclass(exctype):
        exctype = type(exctype)
    res = ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, ctypes.py_object(exctype))
    if res == 0:
        raise ValueError("")
    elif res != 1:
        ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, None)
        raise SystemError("")
 
def runStop():
    _async_raise(thread.ident, SystemExit)

def runConnect(filesDir):
    adbkey = str(filesDir) + "/adbkey"

    if not (Path(adbkey).is_file()):
        keygen(adbkey)

    with open(adbkey) as f:
        priv = f.read()

    with open(adbkey + ".pub") as f:
        pub = f.read()

    signer = PythonRSASigner(pub, priv)

    global device
    device = AdbDeviceTcp(None, 5555, default_transport_timeout_s=9.0)
    device.connect(rsa_keys=[signer], auth_timeout_s=90.0)

def opentap(x, y):
    # Friend
    device.shell(f"input tap {x} {y}")
    time.sleep(3)
    # Gift
    device.shell("input tap 540 1599")
    time.sleep(3)
    # Open
    device.shell("input tap 548 2010")
    time.sleep(1)
    # Close
    device.shell("input tap 545 2191")
    time.sleep(3)
    # Close
    device.shell("input tap 545 2191")
    time.sleep(3)

def runOpen(openLimit):
    limit = 0

    while True:
        # Friend 1
        opentap("688", "939")
        limit += 1
        if (limit == openLimit):
            return
        # Friend 2
        opentap("526", "1296")
        limit += 1
        if (limit == openLimit):
            return
        # Friend 3
        opentap("602", "1604")
        limit += 1
        if (limit == openLimit):
            return
        # Friend 4
        opentap("569", "1991")
        limit += 1
        if (limit == openLimit):
            return
        else:
            # Swipe
            device.shell("input touchscreen swipe 250 1000 250 500 400")
            time.sleep(3)
            continue

def runOpenThread(openLimit):
    global thread
    thread = threading.Thread(target = runOpen, args = (openLimit,))
    thread.start()
    thread.join()