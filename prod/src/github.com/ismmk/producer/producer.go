package main

import (
	"github.com/Shopify/sarama"

	"flag"
	"log"
	"net/http"
	"os"
	"strings"
	"time"
	"io/ioutil"
	"fmt"
)
var (
	addr      = flag.String("addr", ":8080", "The address to bind to")
	brokers   = flag.String("brokers", os.Getenv("KAFKA_PEERS"), "The Kafka brokers to connect to, as a comma separated list")
	topic     = flag.String("topic", "access_log", "Kafka topic name")
)

func main() {
	flag.Parse()
	if *brokers == "" {
		flag.PrintDefaults()
		os.Exit(1)
	}
	brokerList := strings.Split(*brokers, ",")
	log.Printf("Kafka brokers: %s", strings.Join(brokerList, ", "))
	server := &Server{
		AccessLogProducer: newAccessLogProducer(brokerList),
	}
	defer func() {
		if err := server.Close(); err != nil {
			log.Println("Failed to close server", err)
		}
	}()
	log.Fatal(server.Run(*addr))
}

type Server struct {
	AccessLogProducer sarama.AsyncProducer
}

func (s *Server) Close() error {
	if err := s.AccessLogProducer.Close(); err != nil {
		log.Println("Failed to shut down access log producer cleanly", err)
	}

	return nil
}

func (s *Server) Handler() http.Handler {
	return s.withAccessLog()
}

func (s *Server) Run(addr string) error {
	httpServer := &http.Server{
		Addr:    addr,
		Handler: s.Handler(),
	}

	log.Printf("Listening for requests on %s...\n", addr)
	return httpServer.ListenAndServe()
}

type Entry struct {
	data[] byte
}
func (entry *Entry) Length() int {
	return len(entry.data)
}
func (entry *Entry) Encode() ([]byte, error) {
	return entry.data, nil
}

func (s *Server) withAccessLog() http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {
		if req.Method != "POST" {
			return
		}
		hah, err := ioutil.ReadAll(req.Body);
		if err != nil {
			fmt.Fprintf(w, "%s", err)
		}
		println(hah)
		// We will use the url as key. This will cause
		// all the access log entries with same url to end up
		// on the same partition.
		s.AccessLogProducer.Input() <- &sarama.ProducerMessage{
			Topic: *topic,
			Key:   sarama.StringEncoder(req.RequestURI),
			Value: &Entry{data:hah},
		}
	})
}


func newAccessLogProducer(brokerList []string) sarama.AsyncProducer {

	// For the access log, we are looking for AP semantics, with high throughput.
	// By creating batches of compressed messages, we reduce network I/O at a cost of more latency.
	config := sarama.NewConfig()
	config.Producer.RequiredAcks = sarama.WaitForLocal       // Only wait for the leader to ack
	config.Producer.Compression = sarama.CompressionSnappy   // Compress messages
	config.Producer.Flush.Frequency = 500 * time.Millisecond // Flush batches every 500ms


	for  {
		producer, err := sarama.NewAsyncProducer(brokerList, config)
		if err == nil {
			// We will just log to STDOUT if we're not able to produce messages.
			// Note: messages will only be returned here after all retry attempts are exhausted.
			go func() {
				for err := range producer.Errors() {
					log.Println("Failed to write access log entry:", err)
				}
			}()
			return producer
		}
		log.Println("Failed to start Sarama producer:", err)
		time.Sleep(2000)
	}
	return nil
}