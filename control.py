import os, sys, time

parameters = sys.argv[1:]
platform = ""

openlimit = 0
giftlimit = 0

if (os.name == "nt"):
    # Windows
    platform = ".\\w-tools\\adb.exe"
elif (os.name == "posix"):
    # Linux
    platform = "./l-tools/adb"

if (len(parameters) > 1):
    sys.exit()

if (len(parameters) == 1):
    if (parameters[0] == "show"):
        # Show Touches
        os.system(f"{platform} shell content insert --uri content://settings/system --bind name:s:show_touches --bind value:i:1")
        sys.exit()
    elif (parameters[0] == "hide"):
        # Hide Touches
        os.system(f"{platform} shell content insert --uri content://settings/system --bind name:s:show_touches --bind value:i:0")
        sys.exit()
    elif (parameters[0] == "event"):
        # Touch Events
        os.system(f"{platform} shell getevent -l")
        sys.exit()
    elif (parameters[0] == "res"):
        # Device Resolution
        os.system(f"{platform} shell wm size")
        sys.exit()
    else:
        sys.exit()

def opentap(x, y):
    # Friend
    os.popen(f"{platform} shell input tap $((16#{x})) $((16#{y}))")
    time.sleep(3)
    # Gift
    os.popen(f"{platform} shell input tap $((16#21c)) $((16#63f))")
    time.sleep(3)
    # Open
    os.popen(f"{platform} shell input tap $((16#224)) $((16#7da))")
    time.sleep(1)
    # Close
    os.popen(f"{platform} shell input tap $((16#221)) $((16#88f))")
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
    os.popen(f"{platform} shell input tap $((16#130)) $((16#6c9))")
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

def scroll(open, gift, hasgift):
    # Swipe
    os.popen(f"{platform} shell input touchscreen swipe 250 1000 250 500 400")
    time.sleep(1)
    friends(open, gift, hasgift)

def friends(open, gift, hasgift):
    if (open == 1):
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
            scroll(open, gift, hasgift)
    else:
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
            scroll(open, gift, hasgift)

print("")
print("PokeGO Touch".center(os.get_terminal_size().columns))
print("By LillieH1000".center(os.get_terminal_size().columns))
print("Version: 6".center(os.get_terminal_size().columns))
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