package main

import (
	"encoding/xml"
	"io/ioutil"
	"log"
	"net/http"
)

func checkErr(err error) bool {
	if err != nil {
		log.Println(err)
		return true
	}
	return false
}

func rss() {
	req, err := http.NewRequest("GET", "http://blog.csdn.net/archi_xiao/rss/list", nil)
	if checkErr(err) {
		return
	}
	req.Header.Add("Content-Type", "text/xml; charset=utf-8")
	client := http.Client{}

	resp, err := client.Do(req)
	if checkErr(err) {
		return
	}
	b, err := ioutil.ReadAll(resp.Body)
	if checkErr(err) {
		return
	}
	var ret map[string]interface{}
	err = xml.Unmarshal(b, &ret)
	if checkErr(err) {
		return
	}
	log.Println(string(b))
	log.Println(ret)
}

func main() {
	rss()
}
