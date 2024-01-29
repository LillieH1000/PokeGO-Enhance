import os, sys, time

parameters = sys.argv[1:]
platform = ""

openlimit = 0
giftlimit = 0

if (len(parameters) == 0):
    sys.exit()

if (os.name == "nt"):
    platform = ".\\w-tools\\adb.exe"
elif (os.name == "posix"):
    platform = "./l-tools/adb"

if (parameters[0] == "show"):
    os.popen(f"{platform} shell content insert --uri content://settings/system --bind name:s:show_touches --bind value:i:1")

if (parameters[0] == "hide"):
    os.popen(f"{platform} shell content insert --uri content://settings/system --bind name:s:show_touches --bind value:i:0")

if (parameters[0] == "event"):
    os.system(f"{platform} shell getevent -l")

def opentap(x, y):
    os.popen(f"{platform} shell input tap $((16#{x})) $((16#{y}))")
    time.sleep(3)
    os.popen(f"{platform} shell input tap $((16#21c)) $((16#63f))")
    time.sleep(3)
    os.popen(f"{platform} shell input tap $((16#224)) $((16#7da))")
    time.sleep(20)
    os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
    time.sleep(3)

def sendtap(x, y, hasgift):
    os.popen(f"{platform} shell input tap $((16#{x})) $((16#{y}))")
    time.sleep(3)
    if (hasgift == 1):
        os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
        time.sleep(3)
    os.popen(f"{platform} shell input tap $((16#130)) $((16#6c9))")
    time.sleep(3)
    os.popen(f"{platform} shell input tap $((16#272)) $((16#34f))")
    time.sleep(3)
    os.popen(f"{platform} shell input tap $((16#220)) $((16#7c5))")
    time.sleep(5)
    os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
    time.sleep(3)

def scroll(open, gift, hasgift):
    os.popen(f"{platform} shell input touchscreen swipe 250 1000 250 500 400")
    time.sleep(1)
    friends(open, gift, hasgift)

def friends(open, gift, hasgift, openlimit=openlimit, giftlimit=giftlimit):
    if (open == 1):
        # Friend 1
        opentap("2b0", "3ab")
        # Friend 2
        opentap("20e", "510")
        # Friend 3
        opentap("25a", "644")
        # Friend 4
        opentap("239", "7c7")
        openlimit += 1
        if (openlimit == 5):
            sys.exit()
        else:
            scroll(open, gift, hasgift)
    else:
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
            scroll(open, gift, hasgift)

if (parameters[0] == "open"):
    friends(1, None, None)

if (parameters[0] == "send"):
    friends(0, int(parameters[1]), int(parameters[2]))