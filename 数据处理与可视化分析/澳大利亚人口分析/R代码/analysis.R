library(shiny)
library(openxlsx)
library(tidyr)
library(dplyr)
library(leaflet)
library(leaflet.minicharts)
library(shinydashboard)
library(plotly)
#read dataset
data <- read.xlsx("1.xlsx")
data_males <- data[(2:9),]
data_females <-data[(11:18),]
males_list<-gather(data_males,year,population,-X1)%>%filter(year>=2000)
names(males_list) <- c("state","year","population")
males_list$Gender<-"Males"
females_list<-gather(data_females,year,population,-X1)%>%filter(year>=2000)
names(females_list) <- c("state","year","population")
females_list$Gender<-"Females"
data_final <- merge(males_list,females_list,all=TRUE,sort = TRUE)
data_final
#statistics for age group
age_group<-read.csv("age_group.csv")
#population change
population_change <- read.xlsx("population change.xlsx")
population_change <- gather(population_change,year,population,3:47,-
                              state)%>%filter(year>=2000)%>%drop_na()
a <- data_final %>% group_by(year,state) %>%
  summarise(Population=sum(population))
b <- data_final %>% group_by(year) %>%
  summarise(Population=sum(population))
c <- data_final %>% group_by(year, Gender) %>%
  summarise(Population=sum(population))
d<-merge(b,c,all=TRUE,sort = TRUE)
d[is.na(d)]<-"Total"
bbb<-
  population_change%>%group_by(year,type)%>%summarise(Population=sum
                                                      (population))
# rate
all<-merge(b,bbb,all=TRUE,sort = TRUE)
all[is.na(all)]<-"Total"
wide<-spread(all,type,Population)
names(wide)<-
  c("Year","Births","Deaths","Net.InterState.Migration","Net.Overseas.Migration",
    "Total")
dat <- read.csv("table.csv")
Births<-wide[,2]
Deaths<-wide[,3]
migration<- wide[,5]
Total<-wide[,6]
Deaths_rate<-Deaths/Total*1000
Births_rate<-Births/Total*1000
migration_rate <-migration/Total*1000
growth_rate<-matrix()
for (i in (2:16)){
  
  growth_rate[i]<-(Total[i]-Total[i-1])/Total[i]*1000
}
growth_rate[1]<-11.5890
table <-
  data.frame(year=c(2000:2015),Deaths_rate,Births_rate,migration_rate,growth_rate)
table1<-males_list<-gather(table,type,value,-year)
lmamount = lm(growth_rate ~ Deaths_rate+Births_rate+migration_rate, data =
                table)



#ui 
ui <- fluidPage(
  #create dashboard
  dashboardPage(
    #dashboard Header 
    dashboardHeader(
      title = "The data exploration of Australian population",titleWidth = 400
    ),
    #menu
    dashboardSidebar(
      #set item name and set a id which will use in dashboardBody() to the number of menuItem will match to the tabItem
      sidebarMenu(
        menuItem("Home page",tabName = "home",icon = icon("home")),
        menuItem("Demographic analysis",tabName = "plot1",icon = icon("table")),
        menuItem("Population of each state",tabName = "plot2",icon = icon("table")),
        menuItem("Growth,Fertility,Mortality rate",tabName = "plot3",icon = icon("table")),
        menuItem("Dataset",tabName = "Dataset",icon = icon("table"))
        
      )
    ),
      
    #set body of each tab
    dashboardBody(
      #home page, use the menuItem() id called "home"
      tabItems(
        tabItem(tabName = "home",
                #create a new line
                p(h1("welcome to The data exploration of Australian population")),h4("This project aim to explore a completed and comprehensive understanding of",strong("the population tendency in Australia")),
                #add a space line"
                hr(),
                h4("Using regression to find out what the factors will have an effect on population"),
                hr(),
                h4("You can select from",strong("Dashboard"),"go to the each page"),
                hr(),
                h4("Each page you can move your cursor over the picture, then you can see the acutual value for that point"),
                hr(),
                h4("Now, it is time to explore the information by yourself!")
                ),
                
        tabItem(tabName = "plot1",
                h4("This page is used for",strong("demographic analysis")),
                hr(),
                #em() to highlight the words
                h4(em(strong("Help:"))," You can select a type that they want to inspect. After select, the line chart will exchange based on the selected type."),
                h4("Move your cursor over the picture to see the value"),
                p(h2("Changing tendency for different age group")),
                #select bar
                selectInput("type",
                            "Select a type",
                            choices = c(
                              "Births"="Births(b)",
                              "Death"="Deaths(b)",
                              "Net interstate migration1"="Net interstate migration",
                              "Net overseas migration1"="Net overseas migration(c) "
                            )),
                #interface setting fulidRow() create a new row and use plotlyOutput() sign to the server filed
                fluidRow(column(8,plotlyOutput("tendency",height=400))),
                fluidRow(column(4,h2("Changing tendency of age Group")),column(6,h2("Changing tendency of gender percentage"))),
                fluidRow(column(4,plotlyOutput("age")),column(4,plotlyOutput("gender")))
                ),
        tabItem(tabName = "plot2",
                h4("This page is used for",strong("explore the population for each state page")),
                hr(),
                h4(em(strong("Help:"))," You can select a specific year that they want to see by sliding the select bar(See Figure 10). After selecting, the population of each state will be drawn by a different color and the population of male, female will show in the bar chart above each state."),
                h4("Move your cursor over the picture to see the value"),
                fluidRow(column(3,sliderInput("year_select","Select a year",value = 2000,min=2000,max=2015))
                ),
                fluidRow(column(7,leafletOutput("map",height=700)))),
        tabItem(tabName = "plot3",
                h4("This page is used for",strong("Regression Growth,Fertility,Mortality rate")),
                hr(),
                h4(em(strong("Help:"))," You can select a rate type from selection bar. After selecting, the selected type and the growth rate will show in the chart."),
                h4("  Click regression button to see the result and regression line."),
                h4("Move your cursor over the picture to see the value"),
                fluidRow(column(3,selectInput("Type",
                                      "Select a type",
                                      choices = c(
                                        "fertility rate"="Births_rate",
                                        "mortality rate"="Deaths_rate",
                                        "migration rate"="migration_rate"
                                      )))),
                fluidRow(column(6,plotlyOutput("rate"))),
                hr(),
                #add a click button
                fluidRow(column(3,actionButton(inputId = "regression",label = "Regression"))),
                hr(),
                fluidRow(column(6,textOutput("text1"))),
                fluidRow(column(6,textOutput("text2"))),
                fluidRow(column(6,plotOutput("Regression")))
                ),
                
        tabItem(tabName = "Dataset", 
                h4("This page is used for",strong("Access dataset")),
                hr(),
                h4(em(strong("Help:"))," Clicks",em("Australia Bureau of Statistics"),"you will go to the websit to download dataset."),
                #a() add a link 
                tags$p(style= "font-family:Impact",h2("The dataset are from",
                       tags$a("Australia Bureau of Statistics", href ="https://www.abs.gov.au/statistics/people/population/historical-population/2016"))),
                #output a table to show dataset
                fluidRow(column(4,dataTableOutput("table1")),
                         column(4,dataTableOutput("table2")),
                         column(4,dataTableOutput("table3"))),
        )
        )
    )
  )
)

server<-function(input, output){

  observe({
    #filter the data from the select bar
    select_rate <-table1%>%filter(type==input$Type|type=="growth_rate")
    #draw the growth,births,death rate line chart
    output$rate<-renderPlotly(ggplot(select_rate,aes(year,value,group=type,label=type))+geom_line(aes(colour=type),size=1)+geom_point(aes(shape=type)))
    
    #filter the data from slide bar to select the year to draw map
    data_year <- data_final%>%filter(year==input$year_select)%>%group_by(state)%>%summarise(Population=sum(population))
    data_year$Longitude<-c(149.012375,151.209900,132.550964,142.702789,138.599503,146.315918,144.964600,117.793221)
    data_year$Latitude<-c(-35.473469,-33.865143,-19.491411,-20.917574,-34.921230,-41.640079,-37.020100,-25.042261)
    
    #using leafleat() to draw the map and add the marker on the map to see the value
    a<-leaflet(data_year)%>%addTiles()%>%setView(130.9792, -29.8135,17, zoom = 4)%>% addMarkers(~Longitude,~Latitude,popup = data_year$Population, label = data_year$Population)
    
    output$map <- renderLeaflet({a})
   
    
    select_tendency <- bbb%>%filter(type == input$type)
    output$tendency<-renderPlotly({p<-ggplot(select_tendency,aes(year,Population,group=type))
                                  p<-p+geom_point(aes(colour=type))+geom_line(aes(colour=type,linetype=type),size=1)+theme(axis.text.x = element_text(angle = 90, hjust = 1))
                        ggplotly(p)
                                })
    #draw the age bar chart 
    output$age<-renderPlotly({ggplot(age_group)+geom_bar(aes(year, population,fill =
    age_group),width=.5, stat="identity",position="stack")+theme(axis.text.x = element_text(angle=90, hjust = 1))})
    output$gender<-renderPlotly(ggplot(c)+
                                geom_bar(aes(x=year,y=Population,fill=Gender),stat="identity",position="dodge")+theme(axis.text.x = element_text(angle = 90, hjust = 1)))
    
    #observe the button click, after click to show the regression results and draw the regression line chart
    observeEvent(input$regression,{
      output$Regression <- renderPlot({
        isolate({
          fit <- lm(growth_rate~., dat[, -c(1,2)])
          p=plot(growth_rate~year, dat, type="l")
          lines(fit$fitted.values~dat$year, col="red")
          legend("topleft", c("actual", "fitted"), lty=c(1,1), col=c("black", "red"))
          return(p)
        })
      })
      # text show the result of regression
      output$text1 <- renderText("   (Intercept)     Deaths_rate     Births_rate  migration_rate ")
      output$text2 <- renderText(lmamount$coefficients)
    })
  })
  # output the table to show the dataset
  output$table1 <-renderDataTable({data_final})
  output$table2 <-renderDataTable({bbb})
  output$table3 <-renderDataTable({age_group})
  
}

shinyApp(ui, server)