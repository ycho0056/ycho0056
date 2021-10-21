from user import *
from paper import *
from addConference import *
from Topics import *
import os
import shutil
import datetime
import time


def author_homepage(email_address):
    display_title()
    print("****** "+email_address+" Author Home screen******")
    print("1. Upload paper to conferences")
    print("2. Status of the paper")
    print("3. Notification")
    print("4. Log out")
    print("Enter option between 1 - 4:")


def upload_paper(conference_list, email, topics, paper_list):
    inform = conference_list
    display_title()
    print("   ******conference List******")
    if len(inform) == 0:
        print("No conference!")
        print("1. Go to home page")
        print("Enter option:")
        validation_page()
        author_homepage(email)
    else:
        #display conference list
        for i in range(len(inform)):
            print(str(i + 1) + ":" + inform[i][0])
        print(str(len(inform) + 1) + ":" + "go to home page")
        print("Enter option:1" + "-" + str(len(inform) + 1))
        print("Select a conference to submit paper:")
        value = validation_select(inform)
        if value == len(inform):
            author_homepage(email)
        else:
            display_title()
            conf_title = inform[value][0]
            conf_topics = inform[value][2] + ", " + inform[value][3] + ", "+inform[value][4]
            conf_reviewer = []
            for i in inform[value][5:]:
                if "@" in i:
                    conf_reviewer.append(i)
            print("   ******Conference Detail******")
            print("Conference title: " + inform[value][0])
            print("Conference topics: " + inform[value][2] + ", " + inform[value][3] + ", "+inform[value][4])
            print("Conference reviewers: " + str(conf_reviewer))
            print("Conference submit deadline:" + inform[value][-2])


            #if the deadline has passed???
            submit_deadline = inform[value][-2]
            conference_topics = inform[value][3]
            interval = (datetime.datetime.today() - datetime.datetime.strptime(submit_deadline, "%d/%m/%Y")).days
            if str(interval) > '0':
                print("\nToday is " + time.strftime("%d/%m/%Y") +
                      "!!! The deadline has passed {} days!!!".format(interval))
                print("You are no longer able to submit the paper!!!")
                print("1. Go to home page")
                print("Enter option:")
                validation_page()
            else:
                conf_title = inform[value][0]
                print("------Upload a PDF/Word file------")
                print("Please enter the file path(eg: /c/desktop/xxx.pdf):")
                isExists = os.path.exists(conf_title)
                while True:
                    file_path = input()
                    # paper_info.append([paper_title, file_path])
                    #if the paper with correct format
                    if (file_path[0:1] == "/" or (file_path[0:1].isalpha() and file_path[1:2] == ":")) \
                            and (file_path[-4: ] =='.pdf' or file_path[-5:] == '.docx'):
                        paper_title=""
                        if '/' in file_path:
                            paper_title = file_path.split('/')[-1]
                        if '\\' in file_path:
                            paper_title = file_path.split('\\')[-1]

                        isUploaded = False
                        for paper in paper_list:
                            if paper_title in paper and conf_title in paper:
                                isUploaded = True
                        if isUploaded == True:
                            print("You have already submitted the paper!!!")
                            print("You are no longer able to submit the paper!!!")
                            print("1. Go to home page")
                            print("Enter option:")
                            validation_page()
                            break
                        else:
                            topic = []
                            index = 1
                            for i in topics:
                                print(str(index) + '. ' + i)
                                index += 1
                            print("You must select 3 topics of your paper!")
                            num = 0
                            while num < 3:
                                value = input("Enter topics of your paper: ")
                                if not value.isdigit():
                                    print("!Error: Unacceptable value entered")
                                    print("Please enter the choose correctly")
                                    continue
                                if int(value) < 1 or int(value) > len(topics):
                                    print("Sorry, Topic choose do not match according to the option given!")
                                    continue
                                else:
                                    topic.append(topics[int(value) - 1])
                                    num += 1

                            if not isExists:
                                os.makedirs(conf_title)
                                shutil.copy(file_path, conf_title)
                            else:
                                shutil.copy(file_path, conf_title)
                            print("------File uploaded successfully!------")
                            new_paper = Paper()
                            new_paper.set_title(paper_title)
                            new_paper.set_conference_name(conf_title)
                            new_paper.set_topics(topic)
                            new_paper.set_author_name(email)
                            paper_list.append([new_paper.get_title(),new_paper.get_conference_name(),new_paper.get_topics(),new_paper.get_status(),new_paper.get_author_name()])
                            display_upload_successful()
                            break
                    else:
                        print("please input correct file path:")
                return paper_list

#22
def display_upload_successful():
    display_title()
    print("    ******Successful******")
    print("!Congratulations! Your paper \n"
          "has been submitted successfully. \n"
          "Please wait for the reviewers to \n"
          "review your paper and the notifi-\n"
          "cation will be sent to chair ! ")
    # author_homepage()

def author_notification(email_address,notification):
    while True:
        index = 1
        for i in notification:
            if i[-1] == email_address:
                print(str(index)+": "+"Chair Name:"+ i[0] +", Paper Title:" + i [1] +", Status:"+ i [2])
                index+=1
        print()
        select = input("Please enter x go to home screen: ")
        print()
        if select == "x":
            break

def author_display(conference_list, email_address, topics, paper_list,notification):
    while True:
        author_homepage(email_address)
        function_select = validation_select([1,2,3])
        if function_select == 0:
            upload_paper(conference_list,email_address, topics, paper_list)
        elif function_select == 1:
            print("Status of the paper")
        elif function_select == 2:
            author_notification(email_address,notification)
        else:
            papers = paper_list
            break
    return papers
