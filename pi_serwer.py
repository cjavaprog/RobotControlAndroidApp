#from gpiozero import Robot
#from signal import pause
import bluetooth
import time
#robot = Robot(left=(5, 6), right=(9, 10))

def connect():
    server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    server_sock.bind(("", bluetooth.PORT_ANY))
    server_sock.listen(1)

    port = server_sock.getsockname()[1]

    uuid = "00001101-0000-1000-8000-00805F9B34FB"

    bluetooth.advertise_service(server_sock, "SampleServer", service_id=uuid,
                                service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
                                profiles=[bluetooth.SERIAL_PORT_PROFILE],
                                # protocols=[bluetooth.OBEX_UUID]
                                )

    print("Waiting for connection on RFCOMM channel", port)

    client_sock, client_info = server_sock.accept()
    print("Accepted connection from", client_info)
    #try:

    while True:
        data1 = client_sock.recv(1024)
        if data1.decode() == 'stop_server':
            break
        print(data1.decode())
        #time.sleep(0.1)
        command = data1.decode()
        '''

        if command == 'forward':
            robot.forward(0.9)
        elif command == 'backward':
            robot.backward(0.9)
        elif command == 'left':
            robot.left(0.9)
        elif command == 'right':
            robot.right(0.9)
        elif command == 'stop':
            robot.stop()
            '''
        
    #except OSError:
    #    pass
    print("Disconnected.")

    client_sock.close()
    server_sock.close()
    print("All done.")


connect()
# robot.stop()
