import cms
import os
import shutil
import time
from user import *

from addConference import *
def reviewer_homepage(email_address):
    print("==============================")
    print(" Conference Management System")
    print("==============================")
    print("******"+email_address +" Reviewer Home screen******")
    print("1. Accept / Reject to the papers")
    print("2. Review the conference papers")
    print("3. Show all papers")
    print("4. Notification")
    print("5. Log out")
    print("Enter option between 1 - 5:")

def review_paper(conference_list, email, paper_score):
    paper_info = []
    conference = conference_list
    inform = []
    display_title()

    print("   ******conference List******")
    if len(conference) == 0:
        print("No conference!")
        print("1. Go to home page")
        print("Enter option:")
        validation_page()
        reviewer_homepage(email)
    else:
        for conf in conference:
            if email in conf[5:]:
                inform.append(conf)
        # display conference list
        for i in range(len(inform)):
            print(str(i + 1) + ":" + inform[i][0])
        print(str(len(inform) + 1) + ":" + "go to home page")
        print("Enter option:1" + "-" + str(len(inform) + 1))
        print("Select a conference to review paper:")
        value = validation_select(inform)

        # verify if enter which option from the list
        if value == len(inform):
            # reviewer_homepage(email)       #select the last option to go back to reviewer homepage
            print("Go back to HOME SCREEN")
        elif str((datetime.datetime.today() - datetime.datetime.strptime(inform[value][-1], "%d/%m/%Y")).days) > '0':      # if select the conference which review deadline has passed, will not allow to review
                print("\nToday is " + time.strftime("%d/%m/%Y") +
                      "!!! The deadline has passed {} days!!!".format((datetime.datetime.today() - datetime.datetime.strptime(inform[value][-1], "%d/%m/%Y")).days))
                print("You are no longer able to reviewer the paper!!!")
                print("1. Go to home page")
                print("Enter option:")
                validation_page()
        else:
            if not os.path.exists(inform[value][0]):
                print("This conference has no paper now!!!")
                print("1. Go to home page")
                print("Enter option:")
                validation_page()
            else:
                file_list = os.listdir(os.getcwd()+"/"+inform[value][0])
                paper_list = []
                for file in file_list:
                    if file[-4: ] =='.pdf' or file[-5:] == '.docx':
                        paper_list.append(file)
                # print(paper_list)
                for i in range(len(paper_list)):
                    print(str(i+1)+":"+paper_list[i])
                print(str(len(paper_list)+1)+":"+"go to previous page")
                print("Select a paper to review:")
                option = validation_select(paper_list)

                if option == len(paper_list):
                    review_paper(conference_list, email, paper_score)

                else:
                    print("You have select " + str(option+1) + "." + paper_list[option])

                    isEvaluated = False
                    for evaluation in paper_score:
                        if paper_list[option] in evaluation and email in evaluation:
                            isEvaluated = True
                    if isEvaluated == True:
                        print("You have already evaluated, you are no longer able to reviewer the paper!!!")
                        print("1. Go to home page")
                        print("Enter option:")
                        validation_page()
                    else:
                        print("Please enter you local path to download this paper to review(eg: /c/desktop):")
                        paper_path = os.getcwd()+'/'+inform[value][0]+'/'+paper_list[option]
                        while True:
                            download_path = input()
                            isExists = os.path.exists(download_path)

                            if download_path[0:1] == "/" or (download_path[0:1].isalpha() and download_path[1:2] == ":"):
                                if not isExists:
                                    os.makedirs(download_path)
                                    shutil.copy(paper_path, download_path)
                                    display_download_successful(download_path)
                                    break
                                else:
                                    shutil.copy(paper_path, download_path)
                                    display_download_successful(download_path)
                                    break
                            else:
                                print("Please enter correct path:")

                        while True:
                            score = input()
                            if score.isdigit() and int(score) > 0 and int(score) <= 10:
                                paper_score.append([inform[value][0],paper_list[option],score,email])
                                paper_name = paper_list[option]
                                send_chair_notification(inform[value],paper_name,paper_score)
                                display_review_successful()
                                break
                            else:
                                print("please enter correct score:")
                        return paper_score
#2

def send_chair_notification(conference,paper_name,paper_score):
    #get reviewer count
    count_reviewer = len(conference) - 7
    count_marked = 0
    for i in paper_score:
        if conference[0] in i and paper_name in i:
            count_marked+=1
    if count_reviewer == count_marked:
        with open("Chair_notification.txt", 'a') as file:
            file.write(conference[1]+','+conference[0]+","+paper_name+","+"All the reviewer have marked the paper!")




def display_review_successful():
    display_title()
    print("         ******Successful******")
    print("            !Congratulations! \n"
          + "Your have reviwed the paper successfully. \n"
          + "The result will be uploaded immediately \n"
          + "and the notification will be sent to chair!")


def display_download_successful(download_path):
    print("------File downloaded successfully!------")
    print("------Please go to " + download_path + " to review------")
    print("------And then go back here to write your evaluation!------")
    # write_evaluation()
    print("Enter your score: between 1 to 10:")


def reviewer_notification(email_address,notification):
    while True:
        index = 1
        for i in notification:
            if i[-1] == email_address:
                print(str(index)+": "+"Conference name: "+ i[0] +" Chair account" + i [1] +" Reviewer account: "+ i [2])
                index+=1
        print()
        select = input("Please enter x go to home screen: ")
        print()
        if select == "x":
            break
        



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

def reviewer_diaplay(conference_list, email_address, paper_score,notification):
    while True:
        reviewer_homepage(email_address)
        function_select = validation_select([1, 2, 3, 4])
        if function_select == 0:
            pass
        elif function_select == 1:
            review_paper(conference_list,email_address,paper_score)
        elif function_select == 2:
            pass
        elif function_select == 3:
            reviewer_notification(email_address,notification)
        else:
            scores = paper_score
            break
    return scores
    # cms.main()


