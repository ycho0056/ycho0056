class topics():

    def __init__(self):
        self.topics = ""

    def add_topics(self,topics,topic):
        self.topics = topics
        self.topics.append(topic)

    def remove_topics(self):
        while True:
            index = 1
            for i in self.topics:
                print(str(index) + '. ' + i)
                index += 1
            value = input("Select which one you want to remove: ")

            if not value.isdigit():
                print("!Error: Unacceptable value entered")
                print("Please enter the choose correctly")
                continue
            elif int(value) < 1 or int(value) > len(self.topics):
                print("!Error: Value out of index")
                print("Please enter the value within 0 to" + str(len(self.topics)))
                continue
            else:
                self.topics.pop(int(value)-1)
                break

    def get_topics(self):
        return topics
