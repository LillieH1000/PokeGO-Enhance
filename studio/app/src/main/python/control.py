from adb_shell.auth.keygen import keygen
from adb_shell.adb_device import AdbDeviceTcp, AdbDeviceUsb
from adb_shell.auth.sign_pythonrsa import PythonRSASigner
from pathlib import Path
import time

device = None

def opentap(x, y):
    # Friend
    device.shell(f"input tap {x} {y}")
    time.sleep(3)
    # Gift
    device.shell("input tap 540 1599")
    time.sleep(3)
    # Open
    device.shell("input tap 548 2010")
    time.sleep(2)
    # Close
    device.shell("input tap 545 2191")
    time.sleep(3)
    # Close
    device.shell("input tap 545 2191")
    time.sleep(3)

def run(filesDir):
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

    # Friend 1
    opentap("688", "939")
    # Friend 2
    opentap("526", "1296")
    # Friend 3
    opentap("602", "1604")
    # Friend 4
    opentap("569", "1991")