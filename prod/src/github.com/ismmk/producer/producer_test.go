package main

import (
	"io"
	"net/http"
	"bytes"
	"net/http/httptest"
	"testing"

	"github.com/Shopify/sarama/mocks"
)

func TestGetRequest(t *testing.T) {

	accessLogProducerMock := mocks.NewAsyncProducer(t, nil)

	// Now, use dependency injection to use the mocks.
	s := &Server{
		AccessLogProducer: accessLogProducerMock,
	}

	defer safeClose(t, s)

	req, err := http.NewRequest("GET", "http://example.com/?data", nil)
	if err != nil {
		t.Fatal(err)
	}
	res := httptest.NewRecorder()
	s.Handler().ServeHTTP(res, req)

	if res.Code != 200 {
		t.Errorf("Expected HTTP status 200, found %d", res.Code)
	}
}


func TestPostRequest(t *testing.T) {

	accessLogProducerMock := mocks.NewAsyncProducer(t, nil)
	accessLogProducerMock.ExpectInputAndSucceed()

	// Now, use dependency injection to use the mocks.
	s := &Server{
		AccessLogProducer: accessLogProducerMock,
	}

	defer safeClose(t, s)

	req, err := http.NewRequest("POST", "http://example.com/?data", bytes.NewReader(make([]byte, 5, 5)))
	if err != nil {
		t.Fatal(err)
	}
	res := httptest.NewRecorder()
	s.Handler().ServeHTTP(res, req)

	if res.Code != 200 {
		t.Errorf("Expected HTTP status 200, found %d", res.Code)
	}
}



func safeClose(t *testing.T, o io.Closer) {
	if err := o.Close(); err != nil {
		t.Error(err)
	}
}