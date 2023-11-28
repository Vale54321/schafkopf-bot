import serial
import socket

def send_data_to_java(data):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(('localhost', 12345))
        s.sendall(data.encode())

ser = serial.Serial('COM6', 9600)  # Adjust COM port accordingly

while True:
    if ser.in_waiting > 0:
        data = ser.readline().decode('utf-8').strip()
        print("Received data from Arduino: ", data)
        
        # Send the data to the running Java program
        send_data_to_java(data)
