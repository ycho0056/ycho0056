class Paper():
    def __init__(self):
        self.paper_title=""
        self.topics = []
        self.conference_name = ""
        self.author_name = ''
        self.status="No evaluate"

    def set_title(self,title):
        self.paper_title = title

    def set_topics(self,topic=[]):
        self.topics=topic

    def set_conference_name(self,conference_name):
        self.conference_name = conference_name

    def set_author_name(self,author_name):
        self.author_name = author_name

    def set_status(self,status):
        self.status = status

    def get_title(self):
        return self.paper_title

    def get_topics(self):
        return self.topics

    def get_conference_name(self):
        return self.conference_name

    def get_author_name(self):
        return self.author_name

    def get_status(self):
        return self.status