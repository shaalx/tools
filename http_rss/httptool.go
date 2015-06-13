package httptool

import (
	"bytes"
	"crypto/md5"
	"crypto/tls"
	"encoding/hex"
	"fmt"
	"io/ioutil"
	"mime/multipart"
	"net/http"
	"net/http/cookiejar"
	"net/url"
)

var Test string

//http请求方法
//URL地址
//请求包
//cookie
//附加头
//返回请求结果和cookie
func Http_request(strUrl string, postDict map[string]string, cookies string, header map[string]string) (string, string, error) {
	body, cookies, err := Http_send(strUrl, postDict, cookies, header)
	respHtml := string(body)
	//fmt.Println(strUrl + ":" + respHtml)
	return respHtml, cookies, err
}
func Http_getfile(strUrl, cookies string, header map[string]string) []byte {
	body, _, _ := Http_send(strUrl, nil, cookies, header)
	return body
}

func Http_send(strUrl string, postDict map[string]string, cookies string, header map[string]string) ([]byte, string, error) {

	var gCurCookieJar *cookiejar.Jar
	gCurCookieJar, _ = cookiejar.New(nil)
	httpClient := &http.Client{Jar: gCurCookieJar}
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}
	httpClient.Transport = tr
	var httpReq *http.Request
	var err error = nil
	if nil == postDict {
		httpReq, _ = http.NewRequest("GET", strUrl, nil)
	} else {
		postValues := url.Values{}
		for postKey, PostValue := range postDict {
			postValues.Set(postKey, PostValue)
		}
		postDataStr := postValues.Encode()
		postDataBytes := []byte(postDataStr)
		postBytesReader := bytes.NewReader(postDataBytes)

		httpReq, _ = http.NewRequest("POST", strUrl, postBytesReader)
		if header["Content-Type"] == "" {
			httpReq.Header.Add("Content-Type", "application/x-www-form-urlencoded")
		}

	}
	httpReq.Header.Add("Cookie", cookies)
	for headerKey, headerValue := range header {
		httpReq.Header.Add(headerKey, headerValue)
	}
	httpResp, err := httpClient.Do(httpReq)
	if err != nil {
		return make([]byte, 1), "", err
	}
	defer httpResp.Body.Close()
	body, errReadAll := ioutil.ReadAll(httpResp.Body)
	if errReadAll != nil {
		return make([]byte, 1), "", errReadAll
	}
	var gCurCookies []*http.Cookie
	gCurCookies = gCurCookieJar.Cookies(httpReq.URL)
	var cookieNum int = len(gCurCookies)
	cookies = ""
	for i := 0; i < cookieNum; i++ {
		var curCk *http.Cookie = gCurCookies[i]
		cookies += curCk.Name + "=" + curCk.Value + ";"
	}

	return body, cookies, nil
}

// Creates a new file upload http request with optional extra params
func FileUploadRequest(uri string, params map[string]string, paramName, filename string, filedata []byte) (string, error) {

	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	for key, val := range params {
		_ = writer.WriteField(key, val)
	}

	part, _ := writer.CreateFormFile(paramName, filename)
	part.Write(filedata)

	err := writer.Close()
	if err != nil {
		return "", err
	}
	request, err := http.NewRequest("POST", uri, body)
	request.Header.Add("Content-Type", writer.FormDataContentType())
	client := &http.Client{}
	resp, err := client.Do(request)
	res, _ := ioutil.ReadAll(resp.Body)
	return string(res), nil
}

func Http_md5(strUrl string) string {
	var rt string = ""
	response, err := http.Get(strUrl)
	if err != nil {

	} else {
		body, _ := ioutil.ReadAll(response.Body)
		temp := md5.Sum(body)
		rt = hex.EncodeToString(temp[:])
	}
	return rt

}
func Testt(t string) {
	Test = t
	fmt.Println("123" + Test)
}
