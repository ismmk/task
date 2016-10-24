FROM golang


RUN mkdir /app
ADD src/github.com/ismmk/producer /app/
WORKDIR /app
RUN go get github.com/Shopify/sarama
RUN go build -o main .
CMD ["/app/main"]

EXPOSE 8080