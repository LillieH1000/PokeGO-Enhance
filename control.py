import atexit, os, signal, sys, time

# Set Global Variables

platform = ""
navigation = 0
openlimit = 0
giftlimit = 0

# Get Platform And Set ADB Location

if (sys.platform == "win32"):
    # Windows
    platform = ".\\w-tools\\adb.exe"
elif (sys.platform == "darwin"):
    # macOS
    platform = "./m-tools/adb"
elif (sys.platform == "linux"):
    # Linux
    platform = "./l-tools/adb"

# Start ADB

os.system(f"{platform} start-server >/dev/null 2>&1")

# Exit Handling

def on_exit():
    os.system(f"{platform} kill-server >/dev/null 2>&1")

def handle_exit(signum, frame):
    sys.exit(0)

atexit.register(on_exit)
signal.signal(signal.SIGTERM, handle_exit)
signal.signal(signal.SIGINT, handle_exit)

# Check If Device Is Connected

device = os.popen(f"{platform} devices").read()
if (len(device.split("\n", 2)[2]) == 0):
    input("Please connect an Android device to use the script, press enter to exit")
    sys.exit()

# Check If Navigation Is Gesture Based And Set Global Variable

gesture = os.popen(f"{platform} shell cmd overlay dump com.android.internal.systemui.navbar.gestural").read()
if (gesture[gesture.find("mIsEnabled"):].splitlines()[0].find("false") == -1):
    navigation = 1

# Input Functions

def opentap(x, y):
    # Friend
    os.popen(f"{platform} shell input tap $((16#{x})) $((16#{y}))")
    time.sleep(3)
    # Gift
    os.popen(f"{platform} shell input tap $((16#21c)) $((16#63f))")
    time.sleep(3)
    if (navigation == 1):
        # Open
        os.popen(f"{platform} shell input tap $((16#224)) $((16#7da))")
    else:
        # Open
        os.popen(f"{platform} shell input tap $((16#245)) $((16#788))")
    time.sleep(1)
    # Close
    os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
    time.sleep(3)
    # Close
    os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
    time.sleep(3)

def sendtap(x, y, hasgift):
    # Friend
    os.popen(f"{platform} shell input tap $((16#{x})) $((16#{y}))")
    time.sleep(3)
    if (hasgift == "yes" or hasgift == "y"):
        # Close
        os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
        time.sleep(3)
    # Send Gift
    os.popen(f"{platform} shell input tap $((16#11b)) $((16#70a))")
    time.sleep(3)
    # Gift Image
    os.popen(f"{platform} shell input tap $((16#272)) $((16#34f))")
    time.sleep(3)
    # Gift
    os.popen(f"{platform} shell input tap $((16#220)) $((16#7c5))")
    time.sleep(1)
    # Close
    os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
    time.sleep(3)
    # Close
    os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
    time.sleep(3)

def friends(open, gift, hasgift):
    if (open == 1):
        while True:
            global openlimit
            # Friend 1
            opentap("2b0", "3ab")
            openlimit += 1
            if (openlimit == gift):
                sys.exit()
            # Friend 2
            opentap("20e", "510")
            openlimit += 1
            if (openlimit == gift):
                sys.exit()
            # Friend 3
            opentap("25a", "644")
            openlimit += 1
            if (openlimit == gift):
                sys.exit()
            # Friend 4
            opentap("239", "7c7")
            openlimit += 1
            if (openlimit == gift):
                sys.exit()
            else:
                # Swipe
                os.popen(f"{platform} shell input touchscreen swipe 250 1000 250 500 400")
                time.sleep(3)
                continue
    else:
        while True:
            global giftlimit
            # Friend 1
            sendtap("2b0", "3ab", hasgift)
            giftlimit += 1
            if (giftlimit == gift):
                sys.exit()
            # Friend 2
            sendtap("20e", "510", hasgift)
            giftlimit += 1
            if (giftlimit == gift):
                sys.exit()
            # Friend 3
            sendtap("25a", "644", hasgift)
            giftlimit += 1
            if (giftlimit == gift):
                sys.exit()
            # Friend 4
            sendtap("239", "7c7", hasgift)
            giftlimit += 1
            if (giftlimit == gift):
                sys.exit()
            else:
                # Swipe
                os.popen(f"{platform} shell input touchscreen swipe 250 1000 250 500 400")
                time.sleep(3)
                continue

# Print UI And Get Inputs

print("")
print("PokeGO Touch".center(os.get_terminal_size().columns))
print("By LillieH1000".center(os.get_terminal_size().columns))
print("Version: 12".center(os.get_terminal_size().columns))
print("")
print("You can press ctrl+c to kill the script anytime in case of an error".center(os.get_terminal_size().columns))
print("")

op1 = input("Would you to open gifts or sends gifts? Type open or o for opening gifts, type send or s for sending gifts: ").lower()
print("")

if (op1 == "open" or op1 == "o"):
    op2 = input("How many gifts would you like to open? Please enter a number: ").lower()
    print("")
    print("Sending inputs, script will auto stop on completion")
    friends(1, int(op2), None)

if (op1 == "send" or op1 == "s"):
    op2 = input("How many gifts would you like to send? Please enter a number: ").lower()
    print("")
    op3 = input("Does the top of the list have gifts? If yes type yes or y, if no type no or n: ").lower()
    print("")
    print("Sending inputs, script will auto stop on completion")
    friends(0, int(op2), op3)