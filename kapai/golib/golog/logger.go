package golog

import (
	"io"
	"log"
	"os"
)

var (
	logErrorFile *log.Logger
)

func init() {
	errorFile, err := os.OpenFile("go.error.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		log.Fatalln("Failed to open error log file:", err)
	}
	logErrorFile = log.New(io.MultiWriter(errorFile, os.Stdout, os.Stderr), "ERROR: ", log.Ldate|log.Ltime)
}

func Error(v ...any) {
	logErrorFile.Println(v)
}
