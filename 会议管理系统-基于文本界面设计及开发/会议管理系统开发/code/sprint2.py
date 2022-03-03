import cms
def get_conference_list(inform):
    # display user list
    # no conference
    if len(inform) == 0:
        display_title()
        print("   ******conference List******")
        print("No conference!")
        print("1. Go to home page")
        print("Enter option:")
        validation_page()
    else:
        while True:
            display_title()
            print("   ******conference List******")
            for i in range(len(inform)):
                print(str(i + 1) + ":" + inform[i][0])
            print(str(len(inform) + 1) + ":" + "go to home page")
            print("Enter option:1" + "-" + str(len(inform) + 1))
            value = validation_select(inform)
            if value == len(inform):
                break
            else:

                display_title()
                print("   ******Conference Detail******")
                print("Conference title: " + inform[value][0])
                print("Conference hosted: " + inform[value][1])
                print("Conference topics:" + inform[value][2]+", "+inform[value][3]+", "+inform[value][4])
                count = len(inform[value])-7
                reviewer =''
                for i in range(5,5+count):
                    reviewer += inform[value][i]+', '
                print("Conference reviewers:" + reviewer)
                print("Conference submit deadline:" + inform[value][-2])
                print("Conference review deadline:" + inform[value][-1])
                print("1. Go to previous page")
                print("Enter option:")
                validation_page()
                continue


def get_user_list(inform):
    # display user list
    if len(inform) == 0:
        display_title()
        print("   ******User List******")
        print("No user register!")
        print("1. Go to home page")
        print("Enter option:")
        validation_page()
    else:
        while True:
            display_title()
            print("   ******User List******")
            for i in range(len(inform)):
                print(str(i+1) + ":" + inform[i][0])
            print(str(len(inform)+1)+":"+"go to home page")
            print("Enter option:1" + "-" + str(len(inform)+1))
            value = validation_select(inform)
            if value == len(inform):
                break
            else:
                display_title()
                count=0
                print("   ******User Detail******")
                if 'Chair' in inform[value]:
                    count += 1
                if 'Author' in inform[value]:
                    count += 1
                if 'Reviewer' in inform[value]:
                    count += 1
                print("User: " + inform[value][0])
                print("Email: " + inform[value][1])
                if 'Admin' in inform[value]:
                    print("Roles:" + inform[value][3])
                else:
                    print("Highest_qualification: " + inform[value][3])
                    print("Occupation: " + inform[value][4])
                    print("Employer details: " + inform[value][5])
                    print("Mobile number: " + inform[value][6])
                if count ==1:
                    print("Roles: "+inform[value][7])
                    print("Interest: " + inform[value][8]+", "+inform[value][9])
                if count ==2:
                    print("Roles: " + inform[value][7]+", "+inform[value][8])
                    print("Interest: " + inform[value][9]+"， "+inform[value][10])
                if count ==3:
                    print("Roles: " + inform[value][7] + ", " + inform[value][8]+ ", " + inform[value][9])
                    print("Interest: " + inform[value][10] + "， " + inform[value][11])
                print("1. Go to previous page")
                print("Enter option:")
                validation_page()
                continue


def validation_select(inform):
    while True:
        value = input()
        if not value.isdigit():
            print("!Error: Unacceptable value entered")
            print("Please enter the choose correctly")
            continue
        elif int(value) < 1 or int(value) > len(inform) + 1:
            print("!Error: Value out of index")
            print("Please enter the value within 0 to" + str(len(inform) + 1))
            continue
        else:
            return int(value) - 1


def validation_page():
    while True:
        value = input()
        if value != '1':
            print("!Error: Unacceptable value entered")
            print("Please enter the choose correctly")
        else:
            break
    return True


def display_title():
    print("==============================")
    print(" Conference Management System")
    print("==============================")


def admin_homepage(email_address):
    print("==============================")
    print(" Conference Management System")
    print("==============================")
    print("****** "+email_address+" Admin Home screen******")
    print("1. User List")
    print("2. Conference List")
    print("3. Paper List")
    print("4. Log out")
    print("Enter option between 1 - 4:")

def display_admin(conference_list,user_list,email_address):
    while True:
        admin_homepage(email_address)
        function_select = validation_select([1,2,3])
        if function_select == 0:
            get_user_list(user_list)
        elif function_select == 1:
            get_conference_list(conference_list)
        elif function_select == 2:
            print("Paper list")
        else:
            break
    cms.main()


