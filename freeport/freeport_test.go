package freeport

import (
	"fmt"
	"net"
	"strconv"
	"testing"
)

// Get a free port.
func Get() (port int, err error) {
	listener, err := net.Listen("tcp", "127.0.0.1:")
	if err != nil {
		return 0, err
	}
	defer listener.Close()

	addr := listener.Addr().String()
	fmt.Println(addr)
	host, portString, err := net.SplitHostPort(addr)
	if err != nil {
		return 0, err
	}
	fmt.Println(host)
	return strconv.Atoi(portString)
}

func TestGet(t *testing.T) {
	port, err := Get()
	t.Log(port, err)
}
