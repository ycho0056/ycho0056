import cms
from Conference import *
import os
from Topics import *



def chair_homepage(email_address):
    print("==============================")
    print(" Conference Management System")
    print("==============================")
    print("****** "+email_address +" Chair Home screen******")
    print("1. Add conference")
    print("2. Modify conference deadline")
    print("3. Evaluate paper")
    print("4. Notification")
    print("5. Log out")
    print("Enter option between 1 - 5:")

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


def validation_select(inform):
    while True:
        value = input()
        if not value.isdigit():
            print("!Error: Unacceptable value entered")
            print("Please enter the choose correctly")
            continue
        elif int(value) < 1 or int(value) > len(inform) + 1:
            print("!Error: Value out of index")
            print("Please enter the value within 1 to " + str(len(inform) + 1))
            continue
        else:
            return int(value) - 1



def add_conference(conference_list,email_address,topics,line):
    user_list = line
    new_conference = Conference()
    new_conference.set_title()
    new_conference.set_topics(topics)
    new_conference.set_hosted(email_address)
    new_conference.set_reviewers(user_list)
    new_conference.set_submission_deadline()
    new_conference.set_reviewer_deadline()
    #check
    print()
    print("   ******New Conference Detail******")
    print("Conference title: " + new_conference.get_title())
    print("Conference hosted: " + new_conference.get_hosted())
    print("Conference topics:" + str(new_conference.get_topics()))
    print("Conference reviewers:" + str(new_conference.get_reviewers()))
    print("Conference submit deadline:" + new_conference.get_submission_deadline())
    print("Conference review deadline:" + new_conference.get_reviewer_deadline())
    print()
    # confirm submit after add
    select = input("Submit  conference and send to the reviewer(Y/N?): ").upper()
    if select == "Y":
        conference_list.append([new_conference.get_title(), new_conference.get_hosted(), new_conference.get_topics(),
                                new_conference.get_reviewers(), new_conference.get_submission_deadline(), new_conference.get_reviewer_deadline()])
        display_add_succeccful()
        send_reviewer_notification(conference_list[-1][0],conference_list[-1][1],conference_list[-1][3])
        return conference_list
    else:
        select = input("Do you want to modify(Y/N?): ").upper()
        if select == "Y":
                print("Conference title: " + new_conference.get_title())
                select = input("want to modify: Y/N:").upper()
                if select == 'Y':
                    new_conference.set_title()

                print("Conference topics:" + str(new_conference.get_topics()))
                select = input("want to modify: Y/N:").upper()
                if select == 'Y':
                    new_conference.set_topics(topics)

                print("Conference reviewers:" + str(new_conference.get_reviewers()))
                select = input("want to modify: Y/N:").upper()
                if select == 'Y':
                    new_conference.set_reviewers(user_list)

                print("Conference submit deadline:" + new_conference.get_submission_deadline())
                select = input("want to modify: Y/N:").upper()
                if select == 'Y':
                    new_conference.set_submission_deadline()
            # print()
                print()
                print("   ******New Conference Detail******")
                print("Conference title: " + new_conference.get_title())
                print("Conference hosted: " + new_conference.get_hosted())
                print("Conference topics:" + str(new_conference.get_topics()))
                print("Conference reviewers:" + str(new_conference.get_reviewers()))
                print("Conference submit deadline:" + new_conference.get_submission_deadline())
                print("Conference review deadline:" + new_conference.get_reviewer_deadline())
                print()
                select = input("Submit  conference and send to the reviewer(Y/N?): ").upper()
                if select == "Y":
                    conference_list.append([new_conference.get_title(), new_conference.get_hosted(), new_conference.get_topics(),
                         new_conference.get_reviewers(), new_conference.get_submission_deadline(),
                         new_conference.get_reviewer_deadline()])
                    display_add_succeccful()
                    send_reviewer_notification(conference_list[-1][0],conference_list[-1][1],conference_list[-1][3])
                    return conference_list
                else:
                    display_cancel()
                    return conference_list
        else:
            display_cancel()
            return conference_list



def modify_conference(conference_list_all,email_address):
    conference_list = []
    for conference in conference_list_all:
        if conference[1] == email_address:
            conference_list.append(conference)

    if len(conference_list) == 0:
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
            for i in range(len(conference_list)):
                print(str(i + 1) + ":" + conference_list[i][0])
            print(str(len(conference_list) + 1) + ":" + "go to home page")
            print("Enter option:1" + "-" + str(len(conference_list) + 1))
            value = validation_select(conference_list)
            if value == len(conference_list):
                break
            else:
                display_title()
                print("   ****** "+conference_list[value][0]+" Detail******")
                print("1. Current review deadline: "+conference_list[value][-1])
                print("2. Current submit deadline: " + conference_list[value][-2])
                print("3. Go to previous page")
                print("Enter option between 1-3: ")
                select = validation_select([1,2,3])
                if select == 0:
                    print("Please enter new  reviewer deadline: ")
                    print("Hint: The deadline for reviewe is 15 days after the submission deadline!")
                    conference_list[value][-1] = Conference().reviwer_deadline_validation(conference_list[value][-2])
                    display_modify_deadline_succeccful()
                    continue
                if select == 1:
                    deadline = input("Enter submission deadline (eg. dd/mm/yy)?:")
                    while Conference().deadline_Validation(deadline):
                        print("Please enter Valid Date:")
                        deadline = input("Enter submission deadline (eg. dd/mm/yy)?:")
                    conference_list[value][-2] = deadline
                    display_modify_deadline_succeccful()
                    continue
    return conference_list


def evaluate_paper(conference_list_all,email_address,paper_score,chair_notification,author_notification,paper_info):

    conference_list = []
    for conference in conference_list_all:
        if conference[1] == email_address:
            conference_list.append(conference)

    if len(conference_list) == 0:
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
            for i in range(len(conference_list)):
                print(str(i + 1) + ":" + conference_list[i][0])
            print(str(len(conference_list) + 1) + ":" + "go to home page")
            print("Enter option:1" + "-" + str(len(conference_list) + 1))
            value = validation_select(conference_list)
            if value == len(conference_list):
                break
            else:
                if not os.path.exists(conference_list[value][0]):
                    print("This conference has no paper now!!!")
                    print("1. Go to home page")
                    print("Enter option:")
                    validation_page()
                else:
                    file_list = os.listdir(os.getcwd() + "/" + conference_list[value][0])
                    paper_list = []
                    for file in file_list:
                        if file[-4:] == '.pdf' or file[-5:] == '.docx':
                            paper_list.append(file)
                    for i in range(len(paper_list)):
                        print(str(i + 1) + ":" + paper_list[i])
                    print(str(len(paper_list) + 1) + ":" + "go to previous page")
                    print("Select a paper to review:")
                    option = validation_select(paper_list)
                    if option == len(paper_list):
                        evaluate_paper(conference_list, email_address, paper_score,chair_notification,author_notification,paper_info)
                    # check if chair have evaluated
                    find_evaluate = False
                    y=0
                    for y in author_notification:
                        if email_address in y and paper_list[option] in y:
                            find_evaluate = True
                        else:
                            continue
                    if find_evaluate == False:
                        #check if all reviewers have marked
                        find = False
                        x=0
                        for x in chair_notification:
                            if conference_list[value][0] in x and paper_list[option] in x:
                                find = True
                            else:
                                continue

                        if find == True:
                            print()
                            display_title()
                            print("******" + conference_list[value][0] + "Conference Paper value ******")
                            print("Conference name: " + conference_list[value][0] + '\n' +
                                  "Paper title: " + paper_list[option])
                            index = 1
                            for score in paper_score:
                                if conference_list[value][0] in score and paper_list[option] in score:
                                    print(str(index) + ". Reivewer: " + score[-1] + " Mark: " + score[-2])
                                    index += 1
                            email_address_author = ""
                            for paper in paper_info:
                                if paper_list[option] in paper:
                                    email_address_author = paper[-1]
                            print()
                            print("1.Accept")
                            print("2.Reject")
                            print("Enter option:")
                            select = validation_select([1])
                            if select == 0:
                                for paper in paper_info:
                                    if paper_list[option] in paper:
                                        paper[-2] = "Accept"
                                send_author_notification(paper_list[option], email_address,"Accept",email_address_author)
                                display_evaluate_succeccful()
                            else:
                                for paper in paper_info:
                                    if paper_list[option] in paper:
                                        paper[-2] = "Reject"
                                send_author_notification(paper_list[option],email_address,"Reject",email_address_author)
                                display_evaluate_succeccful()
                        else:
                            print("Sorry, all the reviewer haven't reviewed the paper!")
                            print()
                            select = input("Please enter x go to home screen: ")
                            print()
                            if select == "x":
                                break
                    else:
                        print("Sorry, You have evaluate this paper!")
                        print()
                        select = input("Please enter x go to home screen: ")
                        print()
                        if select == "x":
                            break
        return paper_info
#23


def send_author_notification(paper_title,email_address,result,email_address_author):
    with open("Author_notification.txt", 'a') as file:
        file.write(email_address+','+paper_title+","+result+","+email_address_author+"\n")


def chair_notification(email_address,notification):
    while True:
        display_title()
        print("   ****** "+email_address+" Notification******")
        index = 1
        for i in notification:
            if email_address in i:
                print(str(index) + ": " + "Conference name: " + i[1] + " Paper Title: " + i[2] +'\n'+i[3])
                index+=1
        print()
        select = input("Please enter x go to home screen: ")
        print()
        if select == "x":
            break



def display_cancel():
    print()
    display_title()
    print("    ******Successful******")
    print("You have successfully cancel the submition")
    print()

def display_add_succeccful():
    print()
    display_title()
    print("    ******Successful******")
    print("!Congratulations! You \n"
      "have created a conference successfully. \n"
      "The notification will send to reviewer immediately!\n")
    print()

def display_evaluate_succeccful():
    print()
    display_title()
    print("    ******Successful******")
    print("!Congratulations! You \n"
      "have evaluate the paper successfully. \n"
      "The notification will send to author immediately!\n")
    print()


def display_modify_deadline_succeccful():
    print()
    display_title()
    print("    ******Successful******")
    print("!Congratulations! You \n"
          "have modified the conference deadline successfully. \n"
          "The notification will send to reviewer and author immediately!\n")
    print()


def send_reviewer_notification(conference_name,email_address,reviewer_list):
    with open("Reviewer_notification.txt",'a') as file:
        account = 1
        while account <= len(reviewer_list):
            for reviewer_account in reviewer_list:
                file.write(conference_name+","+email_address+","+reviewer_account+"\n")
                account+=1
#

def display_chair(conference_list,email_address,topics,line,paper_score,notification,author_notification,paper_info):
    while True:
        chair_homepage(email_address)
        function_select = validation_select([1,2,3,4])
        if function_select == 0:
            conferences = add_conference(conference_list,email_address,topics,line)
        elif function_select == 1:
            conferences = modify_conference(conference_list,email_address)
        elif function_select == 2:
            evaluate_paper(conference_list,email_address,paper_score,notification,author_notification,paper_info)
        elif function_select ==3:
            chair_notification(email_address,notification)
        else:
            conferences = conference_list
            break
    return conferences,paper_info

