package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

type Customer struct {
	Id   string `json:"id"`
	Name string `json:"name"`
	Iban string `json:"iban"`
}

func CustomerHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	id := vars["id"]

	println("Loading customer " + id)

	customer := loadCustomer(id)

	_ = json.NewEncoder(w).Encode(customer)
}

func loadCustomer(id string) Customer {
	println("Loading customers.json")
	dat, _ := ioutil.ReadFile("customers.json")

	println("Unmarshalling json")
	var customers []Customer
	_ = json.Unmarshal(dat, &customers)

	println(fmt.Sprintf("%d%s", len(customers), " customers loaded"))

	for _, customer := range customers {
		if customer.Id == id {
			return customer
		}
	}

	return Customer{}
}

func main() {
	router := mux.NewRouter()
	router.HandleFunc("/api/customer/{id}", CustomerHandler).Methods("GET")
	log.Fatal(http.ListenAndServe(":8080", router))
}
