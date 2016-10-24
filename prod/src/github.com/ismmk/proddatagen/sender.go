package main

import (
	"flag"
	"net/http"
	"time"
	"math/rand"
	"bytes"
	"encoding/json"
)
var (
	url       = flag.String("url", "http://192.168.99.100:8080", "The address of")
	count     = flag.Int("count", 20 * 1000, "Messages count")
)

func main() {
	flag.Parse()
	clients := []string{"client_1", "client_2", "client_3", "client_4", "client_5"}
	users := []string{"user_1", "user_2", "user_3", "user_4", "user_5"}
	entryType := []string{"type_1", "type_2", "type_3", "type_4", "type_5"}
	params := []string{"param_1", "param_2", "param_3", "param_4", "param_5"}
	paramsVal := []string{"paramVal_1", "paramVal_2", "paramVal_3", "paramVal_4", "paramVal_5"}
	client := &http.Client{}
	start := time.Now().Unix()
	for i := 0; i < *count; i++ {
		 ts := time.Now().Unix() - int64(rand.Intn(2 * 24 * 60 * 60))
		 params := map[string]string{
			 params[rand.Intn(5)] : paramsVal[rand.Intn(5)],
		 }
		 req := &Request{users[rand.Intn(5)], clients[rand.Intn(5)], entryType[rand.Intn(5)], ts, params}

		 data, err := json.Marshal(req)

		request, err := http.NewRequest("POST", *url, bytes.NewReader(data))
		request.Header.Set("Content-Type", "application/json")

		_, err = client.Do(request)
		if err != nil {
			panic(err)
		}

		if i % 5000 == 0{
			println((time.Now().Unix() - start))
		}
	}
}

type Request struct {
	UserId string            `json:"userId"`
	ClientId string          `json:"clientId"`
	EntryType string         `json:"entryType"`
	ActionType int64         `json:"actionTime"`
	Params map[string]string `json:"params"`
}

