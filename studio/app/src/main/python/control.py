from adb_shell.auth.keygen import keygen
from adb_shell.adb_device import AdbDeviceTcp, AdbDeviceUsb
from adb_shell.auth.sign_pythonrsa import PythonRSASigner
from pathlib import Path

def run(filesDir):
    adbkey = str(filesDir) + "/adbkey"

    if not (Path(adbkey).is_file()):
        keygen(adbkey)

    with open(adbkey) as f:
        priv = f.read()

    with open(adbkey + ".pub") as f:
        pub = f.read()

    signer = PythonRSASigner(pub, priv)

    device = AdbDeviceTcp(None, 5555, default_transport_timeout_s=90.0)
    device.connect(rsa_keys=[signer], auth_timeout_s=90.0)

    device.shell("input touchscreen swipe 250 1000 250 500 400")