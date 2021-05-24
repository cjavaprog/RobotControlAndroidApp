import os
from gpiozero import LED
import bluetooth
import time
import concurrent.futures
import RPi.GPIO as GPIO

#Deklaracje pinów, wejść, wyjść, etc.
GPIO.setmode(GPIO.BCM)

pwmFreq = 100

GPIO.setup(18, GPIO.OUT)
GPIO.setup(24, GPIO.OUT)
GPIO.setup(23, GPIO.OUT)
GPIO.setup(25, GPIO.OUT)
GPIO.setup(22, GPIO.OUT)
GPIO.setup(27, GPIO.OUT)
GPIO.setup(17, GPIO.OUT)

pwma = GPIO.PWM(18, pwmFreq)
pwmb = GPIO.PWM(17, pwmFreq)
pwma.start(100)
pwmb.start(100)

GPIO_TRIGGER = 14
GPIO_TRIGGER2 = 26
GPIO_ECHO = 7
GPIO_ECHO2 = 8
GPIO_LIGHT = 12
GPIO_BUZZER =16

GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
GPIO.setup(GPIO_ECHO, GPIO.IN)
GPIO.setup(GPIO_TRIGGER2, GPIO.OUT)
GPIO.setup(GPIO_ECHO2, GPIO.IN)
GPIO.setup(GPIO_BUZZER,GPIO.OUT)

led1 = LED(21)
led2 = LED(20)

disconnected = 0
dist = 0
dist2 = 0

command = ""

#Funkcja migająca diodami
def blink():
    led1.on()
    led2.on()
    time.sleep(0.15)
    led1.off()
    led2.off()
    time.sleep(0.15)

#Funkcja do pomiaru natężenia światła
def light (GPIO_LIGHT):
    count = 0

    GPIO.setup(GPIO_LIGHT, GPIO.OUT)
    GPIO.output(GPIO_LIGHT, GPIO.LOW)
    time.sleep(0.1)

    #Change the pin back to input
    GPIO.setup(GPIO_LIGHT, GPIO.IN)

    #Count until the pin goes high
    while (GPIO.input(GPIO_LIGHT) == GPIO.LOW):
        count += 1

    return count

#Funkcja do włączania diód, jeśli zostanie przekroczona wartość ustalona na 25000
def measure_light():
    while True:
        time.sleep(0.5)
        brightness = light(GPIO_LIGHT)
        print(brightness)
        if(brightness > 25000):
            led1.on()
            led2.on()
        else:
            led1.off()
            led2.off()
        if disconnected == 1:
            led1.off()
            led2.off()
            break;

#Funkcje odpowiadające za pomiar odległości od obiektu przez oba czujniki
def distance():
    time.sleep(0.8)
    GPIO.output(GPIO_TRIGGER, True)

    time.sleep(0.00001)
    GPIO.output(GPIO_TRIGGER, False)

    StartTime = time.time()
    StopTime = time.time()

    while GPIO.input(GPIO_ECHO) == 0:
        StartTime = time.time()

    while GPIO.input(GPIO_ECHO) == 1:
        StopTime = time.time()

    TimeElapsed = StopTime - StartTime
    # mnożenie przez prędkość dźwięku (34300 cm/s) i podzielenie przez 2, bo tam i z powrotem
    distance = (TimeElapsed * 34300) / 2

    return distance

def measure_dist():
    while True:
        global dist
        dist = distance()
        #print ("Dstans od czujnika 1 = %.1f cm" % dist)
        if dist < 10.0 or dist2 < 30:
            GPIO.output(GPIO_BUZZER, GPIO.HIGH)
        else:
            GPIO.output(GPIO_BUZZER, GPIO.LOW)

        if disconnected == 1:
            break



def distance2():
    time.sleep(0.8)
    GPIO.output(GPIO_TRIGGER2, True)

    time.sleep(0.00001)
    GPIO.output(GPIO_TRIGGER2, False)

    StartTime2 = time.time()
    StopTime2 = time.time()

    while GPIO.input(GPIO_ECHO2) == 0:
        StartTime2 = time.time()

    while GPIO.input(GPIO_ECHO2) == 1:
        StopTime2 = time.time()

    TimeElapsed2 = StopTime2 - StartTime2
    distance2 = (TimeElapsed2 * 34300) / 2

    return distance2

def measure_dist2():
    while True:
        global dist2
        dist2 = distance2()
        #print ("Dystans od czujnika 2 = %.1f cm" % dist2)

        if dist2 < 30.0 or dist < 10:
            GPIO.output(GPIO_BUZZER, GPIO.HIGH)
        else:
            GPIO.output(GPIO_BUZZER, GPIO.LOW)

        if disconnected == 1:
            break

#Funkcje służące do poruszania silnikami, odpowiednio w przód i w tył
def forward(spd):
    runMotor(0, spd, 0)
    runMotor(1, spd, 0)

def reverse(spd):
    runMotor(0, spd, 1)
    runMotor(1, spd, 1)

def turnLeft(spd):
    runMotor(0, spd, 0)
    runMotor(1, spd, 1)

def turnRight(spd):
    runMotor(0, spd, 1)
    runMotor(1, spd, 0)

def runMotor(motor, spd, direction):
    GPIO.output(25, GPIO.HIGH);
    in1 = GPIO.HIGH
    in2 = GPIO.LOW

    if(direction==1):
        in1 = GPIO.LOW
        in2 = GPIO.HIGH

    if(motor==0):
        GPIO.output(23, in1)
        GPIO.output(24, in2)
        pwma.ChangeDutyCycle(spd)
    elif(motor==1):
        GPIO.output(22, in1)
        GPIO.output(27, in2)
        pwmb.ChangeDutyCycle(spd)

def motorStop():
    GPIO.output(25, GPIO.LOW)

#Funkcja służąca do utworzenia socketu i oczekiwania na podłączenie smartfona przez aplikację
def connect():
    global disconnected
    disconnected = 0
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

    blink()
    blink()
    blink()

    os.system("echo 0=4% > /dev/servoblaster")
    os.system("echo 1=96% > /dev/servoblaster")
    return client_sock, server_sock

#Funkcja służąca do zczytywania komend przesyłanych przez bluetooth
def bluetooth_monitor(sockett, serv_sockett):
    while True:
        data1 = sockett.recv(1024)
        if data1.decode() == 'stop_server':
            break
        global command
        command = data1.decode()
        print(command)
    print("Disconnected.")
    global disconnected
    disconnected = 1
    sockett.close()
    serv_sockett.close()
    print("All done.")

#Funkcja odpowiadająca za poruszanie robotem oraz łyżką
def move():
    i=0

    while True:
        if disconnected == 1:
            break;

        global command

        if command == 'forward':
            forward(100)
        elif command == 'backward':
            reverse(100)
        elif command == 'left':
            turnLeft(100)
        elif command == 'right':
            turnRight(100)
        elif command == 'stop':
            motorStop()

        elif command == 'lifting':
            if i <= 17:
                os.system("echo 0={}% > /dev/servoblaster".format(i))
                os.system("echo 1={}% > /dev/servoblaster".format(100-i))
                i = i + 1
                print(i)
            else:
                i=17

            time.sleep(0.01)

        elif command == 'lowering':
            if i >= 0:
                os.system("echo 0={}% > /dev/servoblaster".format(i))
                os.system("echo 1={}% > /dev/servoblaster".format(100-i))
                i = i - 1
                print(i)
            else:
                i=0
            time.sleep(0.01)
        elif command == 'stop_lift':
            pass

#Główna funkcja, w której utworzone zostają wątki. Jest zapętlona, dzięki czemu po rozłączeniu się, możemy znów się połączyć, bez ponownego włączania serwera.
def main(args=None):
    while True:

        with concurrent.futures.ThreadPoolExecutor(max_workers=5) as executor:

            sock, serv_sock = connect()
            a = executor.submit(move)
            b = executor.submit(measure_light)
            c = executor.submit(measure_dist)
            d = executor.submit(measure_dist2)
            e = executor.submit(bluetooth_monitor, sock, serv_sock)

            if disconnected == 1:
                GPIO.output(GPIO_BUZZER, GPIO.LOW)
                executor.shutdown(move)
                executor.shutdown(measure_light)
                executor.shutdown(measure_dist)
                executor.shutdown(measure_dist2)
                executor.shutdown(bluetooth_monitor)
                GPIO.cleanup()
        motorStop()

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("Stopped by User")
        motorStop()
        GPIO.output(GPIO_BUZZER,GPIO.LOW)
        GPIO.cleanup()
        led1.off()
        led2.off()
