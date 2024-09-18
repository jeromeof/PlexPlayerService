import socket

EMULATOR_IP = "10.0.2.2"  # Default IP for Android Emulator
EMULATOR_PORT = 32412  # Port on which the emulator sends broadcasts
BROADCAST_PORT = 32412  # Port on which you want to broadcast on the local network

# Create a socket to listen for broadcasts from the emulator
emulator_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
emulator_socket.bind((EMULATOR_IP, EMULATOR_PORT))

# Create a socket to send broadcasts on the local network
broadcast_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
broadcast_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

while True:
    data, addr = emulator_socket.recvfrom(1024)
    broadcast_socket.sendto(data, ('<broadcast>', BROADCAST_PORT))