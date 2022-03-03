import datetime
class Conference:
    def __init__(self):
        self.title=""
        self.hosted=""
        self.topics = []
        self.reviewers = []
        self.submission_deadline = ''
        self.reviewer_deadline = ''
        self.list_paper = []


    def set_title(self):
        self.title=""
        while True:
            title = input("Enter conference title: ")
            trim_title = title.replace(" ", "")
            if trim_title != '' and trim_title.isalpha():
                self.title = title
                break
            else:
                print("Unaccepted input value")
                continue

    def get_title(self):
        return self.title

    def set_hosted(self, email_address):
        self.hosted=email_address

    def get_hosted(self):
        return self.hosted

    def set_topics(self,topics=[]):
        self.topics=[]
        index = 1
        for i in topics:
            print(str(index) + '. ' + i)
            index += 1
        print("You must select 3 topics for this conference!")
        num = 0
        while num < 3:
            value = input("Enter topics for this conference: ")
            if not value.isdigit():
                print("!Error: Unacceptable value entered")
                print("Please enter the choose correctly")
                continue
            if int(value) < 1 or int(value) > len(topics):
                print("Sorry, Topic choose do not match according to the option given!")
                continue
            else:
                self.topics.append(topics[int(value)-1])
                num += 1

    def get_topics(self):
        return self.topics


    def set_reviewers(self,user_list=[]):
        reviewer_list = []
        self.reviewers = []
        for i in user_list:
            if "Reviewer" in i:
                reviewer_list.append(i)
            else:
                continue
        index = 1
        for i in reviewer_list:
            print(str(index) + '. ' + "Reviewer Name: " + i[0] +" "+"Interest Area: "+i[-1]+", "+i[-2])
            index+=1

        while True:
            print("You must select at least 4 reviewer for this conference!" + '\n'
                  + "select number from 4 to " + str(len(reviewer_list)))
            number = input()
            if number.isdigit() and int(number) >= 4 and int(number) <= len(reviewer_list):
                num = 0
                while num < int(number):
                    value = input("Enter reviewer for this conference: ")
                    if not value.isdigit():
                        print("!Error: Unacceptable value entered")
                        print("Please enter the choose correctly")
                        continue
                    if int(value) < 1 or int(value) > len(reviewer_list):
                        print("Sorry, Topic choose do not match according to the option given!")
                        continue
                    else:
                        if reviewer_list[int(value) - 1][-1] in self.topics or reviewer_list[int(value) - 1][-2] in self.topics:
                            self.reviewers.append(reviewer_list[int(value) - 1][1])
                            num += 1
                        else:
                            print("Sorry, this reviewer does not belongs to this area!")
                            continue

            else:
                print("please enter correct number:")
                continue
            break



    def get_reviewers(self):
        return self.reviewers

    def set_submission_deadline(self):
        self.submission_deadline=""
        deadline = input("Enter submission deadline (eg. dd/mm/yy)?:")
        while self.deadline_Validation(deadline):
            print("Please enter Valid Date:")
            deadline = input("Enter submission deadline (eg. dd/mm/yy)?:")
        self.submission_deadline = deadline

    def get_submission_deadline(self):
        return self.submission_deadline

    def set_reviewer_deadline(self):
        self.reviewer_deadline
        self.reviewer_deadline = self.reviwer_deadline_validation(self.submission_deadline)

    def get_reviewer_deadline(self):
        return self.reviewer_deadline

    def set_list_paper(self,list_paper=[]):
        self.list_paper = list_paper

    def get_list_paper(self):
        return self.list_paper

    def deadline_Validation(self,dob):
        # counting "/", if count is 2 then go for further calculation,
        # because of the format requires to use 2 "/"
        if dob.count("/") == 2:
            # splitting the date of birth by "/"
            temp = dob.split("/")
            # iterating over each element
            for each in temp:
                # and check if it is digit.
                if not each.isdigit():
                    # if not digit return true.
                    return True
            # checking for day between 1-31, and month between 1-12
            if (0 < int(temp[0]) < 32) and (0 < int(temp[1]) < 13):
                # defining variables to store month with 30/31 days
                month_30 = ["04", "06", "09", "11"]
                month_31 = ["01", "03", "05", "07", "08", "10", "12"]
                # checking for month for 30 days & days to be in range of 1,30
                if (temp[1] in month_30) and (int(temp[0]) <= 30):
                    # if true, return False
                    return False
                # checking for month for 31 days & days to be in range of 1,31
                elif (temp[1] in month_31) and (int(temp[0]) <= 31):
                    # if true, return False
                    return False
                # check for days is less than 28, assuming feb has 28 days.
                elif int(temp[0]) <= 28:
                    # if true, return False
                    return False
            # return True, if all condition is false.
            return True
        else:
            # return True, for condition of "/" count is false
            return True

    def reviwer_deadline_validation(self,deadline):
        deadline = datetime.datetime.strptime(deadline, "%d/%m/%Y")
        while True:
            review_deadline = input("Enter reviewer deadline (eg. dd/mm/yy)?:")
            if Conference().deadline_Validation(review_deadline):
                print("Please enter Valid Date:")
                continue
            review_deadline = datetime.datetime.strptime(review_deadline, "%d/%m/%Y")
            if review_deadline - deadline < datetime.timedelta(days=15):
                print("The deadline for reviewe is 15 days after the submission deadline!")
                continue
            else:
                break
        return review_deadline.strftime("%d/%m/%Y")
