import this

import user
import sys
from user import *
import sprint2
from reviewer import *
from uploadPaper import *
from addConference import *
from uploadPaper import *
line = []
interest = []
this.conference_list = []
this.topic_list = []
this.paper_list = []
this.paper_score = []
this.chair_notification=[]
this.author_notification=[]
this.reviewer_notification=[]

clear = lambda : os.system('cls')
user_choice = ' '



def main():
    clear()
    user_choice = ' '
    while user_choice != '2':
        print(" Main menu ")
        print("------------")
        print("1. Register")
        print("2. Login")
        print("3. Exit")
        print("------------")
        while True:
            user_choice = input("Enter your choice: ")
            if user_choice not in ['1','2','3']:
                print("Not a valid option ")

            elif user_choice in ['1','2','3']:
                break

        while True:
            if user_choice == '1':
                register()
                break
            if user_choice == '2':
                login()
                break
            if user_choice == '3':
                print("You have logged out from the application.")
                sys.exit()



def register():
    clear()

    while True:
        name = input("Enter name: ")
        set_name(name)
        name = get_name()
        trim_name = name.replace(" ","")
        if name!= '' and trim_name.isalpha():
            break
        else:
            print("The entered value does not only contain strings. Please enter again.")

    while True:
        email_address = input("Enter email address: ")
        set_email_address(email_address)
        email_address = user.get_email_address()
        if email_address != '' and email_address.find('@') != -1 and email_address.find('.') != -1:
            break
        else:
            print("The string entered is either empty or does not contain email in the correct format. Please enter again.")

    while True:
        password = input("Enter password: ")
        set_password(password)
        password = get_password()
        if password != '' and len(password) >= 8:
            break
        else:
            print("The password entered is either empty or has length less than 8. Please enter again.")

    while True:
        highest_qualification = input("Enter highest qualification: ")
        set_highest_qualification(highest_qualification)
        highest_qualification = get_highest_qualification()
        trim_highest_qualification = highest_qualification.replace(" ","")
        if highest_qualification != '' and trim_highest_qualification.isalpha():
            break
        else:
            print("TThe entered value does not only contain strings. Please enter again.")

    while True:
        occupation = input("Enter Occupation: ")
        set_occupation(occupation)
        occupation = get_occupation()
        trim_occupation = occupation.replace(" ", "")
        if occupation != '' and trim_occupation.isalpha():
            break
        else:
            print("The entered value does not only contain strings. Please enter again.")


    while True:
        employer_details = input("Enter Employer Details: ")
        set_employer_details(employer_details)
        employer_details = get_employer_details()
        trim_employer_details = employer_details.replace(" ", "")
        if employer_details != '' and trim_employer_details.isalpha():
            break
        else:
            print("The entered value does not only contain strings. Please enter again.")

    while True:
        mobile_number = input("Enter Mobile Number: ")
        set_mobile(mobile_number)
        mobile_number = get_mobile()
        if mobile_number != '' and mobile_number.isnumeric() and len(mobile_number) == 10:
            break
        else:
            print("Mobile number should be 10 digits long. Please enter again.")

    while True:
        counter_interest = 0
        interest_area = input("Enter the number of Interest Areas: ")
        if interest_area.isnumeric():
            while counter_interest < int(interest_area):
                interest_area_text = input("Enter your Interest Areas: ")
                trim_interest_area = interest_area_text.replace(" ","")
                if trim_interest_area != '' and trim_interest_area.isalpha():
                    interest.append(interest_area_text)
                    counter_interest += 1
                else:
                    print("The entered value does not only contain strings. Please enter again.")
                    continue
        else:
            print("The entered value does not only contain number. Please enter again.")
            continue
        #interest area validation

        set_interest_area(interest)
        for i in interest:
            if i in this.topic_list:
                continue
            else:
                this.topic_list.append(i)
        break


    print("There are three roles that you can have in this system. You can choose to have multiple roles within Chair, Author, Reviewer.")

    while True:
        print("Please enter the number of roles. Enter option between 1 - 3")
        number_of_roles = input("Enter number of roles: ")
        if number_of_roles == '1' or number_of_roles == '2' or number_of_roles == '3':
            global counter
            counter = 0
            number = int(number_of_roles)
            roles = []
            while counter < number:
                choose_role = input("Enter Choose Role (""Chair, Author, Reviewer""): ")
                if choose_role in ['Chair','Author','Reviewer']:
                    if choose_role in roles:
                        print("You have already entered this role before. Please enter another roles")
                        continue
                    else:
                        roles.append(choose_role)
                        counter = counter + 1
                else:
                    print("Role choose do not match according to the option given")
            set_role(roles)
            choose_role = get_role()
            break
        else:
            print("Please enter a valid option.")

    if checkInfo(get_email_address()):
        print()
        print("User already exists please register with new details.")
        main()
    else:
        new = AddAllDetails([name, email_address, password, highest_qualification, occupation, employer_details, mobile_number], interest, choose_role)
        line.append(new[0])
        #addUserInfo([name, email_address, password, highest_qualification, occupation, employer_details, mobile_number, interest_area],choose_role)
        chooseRole(get_email_address(), get_password())

def login():
    clear()
    while True:
        email_address = input("Enter email address: ")
        set_email_address(email_address)
        email_address = get_email_address()
        if email_address != '' and email_address.find('@') != -1 and email_address.find('.') != -1:
            break
        else:
            print("The string entered is either empty or does not contain email in the correct format. Please enter again.")

    while True:
        password = input("Enter password: ")
        set_password(password)
        password = get_password()
        break
    c = 0

    for ep in line:
        if ep[1] == email_address:
            if ep[2] == password:
                if ep[3] == 'Admin':
                        sprint2.display_admin(this.conference_list,line,email_address)


        if ep[1] == email_address and ep[2] == password:
            c += 1
            break
        else:
            continue
    if c == 1:
        print()
        print("Login Successful.")
        print()
        chooseRole(email_address, password)
    else:
        print()
        print("User does not exists. Please select the menu from below.")
        print()
        main()

def checkInfo(email_address):
    for lines in line:
        if lines[1] == email_address:
            return True


def chooseRole(email_address, password):
    c = ''
    a = ''
    r = ''
    count = 0
    index = 0
    for lines in line:
        index+=1
        if lines[1] == email_address and lines[2] == password:
            if 'Chair' in lines:
                c ='Chair'
                count += 1
            if 'Author' in lines:
                a = 'Author'
                count += 1
            if 'Reviewer' in lines:
                r = 'Reviewer'
                count += 1
            break

    for itr in range(count):
        print()
        print("Please choose the option for the role you want to enter with in the system:")

        if c != '':
            print(str(itr+1) + "." + "Chair")
            itr = itr+1
        if a != '':
            print(str(itr+1) + "." + "Author")
            itr = itr+1
        if r != '':
            print(str(itr+1) + "." + "Reviewer")
            itr = itr+1
        break
    counts = []
    for i in range(count):
        counts.append(str(i+1))

    while True:
            print()
            # role_choice = input("Enter your choice: ")
            # if role_choice not in counts:
            #     print("Not a valid option ")
            choose_role = input("Enter Choose Role (""Chair, Author, Reviewer""): ")
            if choose_role not in ['Chair', 'Author', 'Reviewer']:
                print("Role choose do not match according to the option given")
                continue
            else:
                if choose_role in line[index-1]:
                    if choose_role == 'Chair':
                        information = display_chair(this.conference_list,email_address,this.topic_list,line,this.paper_score,this.chair_notification,this.author_notification,this.paper_list)
                        this.conference_list = information[0]
                        this.paper_list = information[1]
                        break
                    if choose_role == 'Author':
                        this.paper_list = author_display(this.conference_list,email_address,this.topic_list, this.paper_list,this.author_notification)
                        break
                    if choose_role == 'Reviewer':
                        this.paper_score = reviewer_diaplay(this.conference_list, email_address, this.paper_score,this.reviewer_notification)
                        break   # input 3 cant work
                else:
                    print("You don't have this role permission!")
                    continue
    write_file(line, this.topic_list, this.conference_list, this.paper_score, this.paper_list)
    main()
                                                # get_name() cant get the user name
                                                # in reviewer_display() the parameter need to pass the user name who log in
#2

#1
read = read_file()
line=read[0]
this.topic_list=read[1]
print(this.topic_list)
this.conference_list =read[2]
this.paper_score = read[3]
this.paper_list = read[4]
this.chair_notification=read[5]
this.author_notification = read[6]
this.reviewer_notification = read[7]
main()


"""
def sanitizeName():
    pass
def hashPassword(password):
    return hashlib.sha256(str.encode(password)).hexdigest()
def checkPassword(password, hash):
    return hashPassword(password) == hash
"""
"""

def addUserInfo(userinfo: list, roles: list):
    user_inf = {}
    line = []
    c = 0
    with open('userInfo.txt', 'a') as file:
        with open('userInfo.txt', 'r') as files:
            for line in files:
                line = line.split(',')
                if line[0] == get_name() and line[1] == get_email_address():
                    c += 1
        if c == 0:
            for info in userinfo:
                file.write(info)
                file.write(',')
            for inf in roles:
                file.write(inf)
                file.write(',')
            file.write('\n')
        else:
            print()
            print("User already exists please register with new details.")
            print()
"""



