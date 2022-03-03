from register import *
from cms import *

lines = []


def AddAllDetails(userinfo: list, interest: list, roles: list):
    up = []
    for info in userinfo:
        up.append(info)
    for role in roles:
        up.append(role)
    lines.append(up)
    for interests in interest:
        up.append(interests)
    return lines

def read_file():
    lines = []
    with open('userInfo.txt', 'a') as file:
        with open('userInfo.txt', 'r') as files:
            for line in files:
                line = line.strip('\n')
                lines.append(line.split(','))

    topics = []
    with open("topics.txt", 'r') as file:
        for lined in file:
            topics.append(lined.strip('\n'))

    conference_list = []
    # get list conference
    with open("conference.txt", 'r') as file:
        for line in file:
            conference_list.append(line.strip('\n').split(','))

    paper_score = []
    # get list conference
    with open("paperscore.txt", 'r') as file:
        for line in file:
            paper_score.append(line.strip('\n').split(','))

    paper_list = []
    # get list conference
    with open("paper.txt", 'r') as file:
        for line in file:
            paper_list.append(line.strip('\n').split(','))

    chair_notification = []
    with open("Chair_notification.txt", "r") as file:
        for line in file:
            chair_notification.append(line.strip('\n').split(','))

    author_notification = []
    with open("Author_notification.txt", "r") as file:
        for line in file:
            author_notification.append(line.strip('\n').split(','))

    reviewer_notification = []
    with open("Reviewer_notification.txt", "r") as file:
        for line in file:
            reviewer_notification.append(line.strip('\n').split(','))

    return lines,topics,conference_list,paper_score,paper_list,chair_notification,author_notification,reviewer_notification

def write_file(userinfo,interest,conference_list,paper_score,paper_list):
    with open('userInfo.txt', 'w') as file:
        for info in userinfo:
            for writing in info:
                file.write(writing)
                if writing==info[-1]:
                    file.write("\n")
                else:
                    file.write(",")

    with open("topics.txt",'w') as file:
        for i in interest:
            file.write(i)
            file.write('\n')

    with open("conference.txt",'w') as file:
        for conference in conference_list:
            for information in conference:
                if information != conference[-1]:
                    if type(information) is not list:
                        file.write(information)
                        file.write(',')
                    else:
                        for i in information:
                         file.write(i)
                         file.write(',')
                else:
                    file.write(information)
                    file.write('\n')

    with open("paperscore.txt", 'w') as file:
        for score in paper_score:
            for information in score:
                if information != score[-1]:
                    if type(information) is not list:
                        file.write(information)
                        file.write(',')
                    else:
                        for i in information:
                         file.write(i)
                         file.write(',')
                else:
                    file.write(str(information))
                    file.write('\n')

    with open("paper.txt", 'w') as file:
        for paper in paper_list:
            for information in paper:
                if information != paper[-1]:
                    if type(information) is not list:
                        file.write(information)
                        file.write(',')
                    else:
                        for i in information:
                         file.write(i)
                         file.write(',')
                else:
                    file.write(str(information))
                    file.write('\n')