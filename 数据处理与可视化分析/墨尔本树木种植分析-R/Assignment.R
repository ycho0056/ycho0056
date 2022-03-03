library(ggplot2)
library(shiny)
library(leaflet)
library(dplyr)

data <- read.csv("PE2_R_Tree_Data.csv")
countTree <- data %>% group_by(Genus) %>% summarise(count=n())%>% arrange(desc(count))
topTree <- countTree[(1:5),]
topTree

ui <- fluidPage(
  h1("Common Trees around Fitzroy Gardens"),
  
  sidebarLayout(
    
    sidebarPanel(
      h2("The Top 5 Trees"),
      p("This graph aims to show the top 5 genera trees by count and
        the map aims to show the locations of top 5 genus"),
      plotOutput("VIS1"),
      h2("Life Expectancy"),
      p("This right side graph aims to show the life expectancy for the top 5 genera by conunt")
      ),
    
    mainPanel(
      selectInput("Type",
                  label = "choose a Genus",
                  choices = list(
                    "all"="all",
                    "Ulmus"="Ulmus",
                    "Platanus"="Platanus",
                    "Quercus"="Quercus",
                    "Corymbia"="Corymbia",
                    "Ficus"="Ficus"
                  )
                ),
      leafletOutput("VIS3"),
      plotOutput("VIS2")
    )
  )
)

server <- function(input, output, session) 
{
  output$VIS1 <- renderPlot(
    ggplot(topTree,aes(Genus,count))+geom_bar(stat="identity",aes(fill=Genus))+
      geom_text(aes(label=count),size=5,position = position_stack(vjust = 0.5))
  )
  
  output$VIS2 <- renderPlot({
    treeDetail <- data %>% filter(Genus=="Ulmus"|Genus=="Platanus"|Genus=="Quercus"|Genus=="Corymbia"|Genus=="Ficus")
  
    ggplot(treeDetail,aes(Useful.Life.Expectancy.Value))+geom_histogram(aes(fill=Genus))+facet_wrap(~Genus)
  })
  
  observe({
  if(input$Type=="all")
  {
    output$VIS3 <- renderLeaflet({
      selectTree <- data %>% filter(Genus=="Ulmus"|Genus=="Platanus"|Genus=="Quercus"|Genus=="Corymbia"|Genus=="Ficus")
      
      pal <- colorFactor(c("red","black","navy","purple","Dark green"),domain = c("Ulmus","Platanus","Quercus","Corymbia","Ficus"))
      
      leaflet(selectTree) %>% addTiles()%>%setView(144.9792, -37.8135,17)%>%
        addCircles(color = ~pal(Genus),radius = ~Diameter.Breast.Height/12, stroke = FALSE,fillOpacity = 0.6)%>%
        addLegend(pal = pal,values = ~Genus)
        })
    }
  else
  {
    output$VIS3 <- renderLeaflet({
      selectTree <- data %>% filter(Genus ==input$Type)
      pal <- colorFactor(c("red","black","navy","purple","Dark green"),domain = c("Ulmus","Platanus","Quercus","Corymbia","Ficus"))
      
      leaflet(selectTree) %>% addTiles()%>%setView(144.9792, -37.8135,17)%>%
        addCircles(color = ~pal(Genus),radius = ~Diameter.Breast.Height/12, stroke = FALSE,fillOpacity = 0.6)%>%
        addLegend(pal = pal, values = ~Genus)
      })
    }
  
  })
}

shinyApp(ui, server)
