Juego UNO – JuegoUnoSoftware
Aplicación de escritorio del juego UNO desarrollada en Java. Permite que una persona juegue contra uno, dos o tres robots que realizan sus jugadas automáticamente. La aplicación cuenta con una interfaz gráfica desarrollada con Java Swing y utiliza Maven para la gestión y compilación del proyecto.
1. Funcionalidades implementadas
- Iniciar una partida desde la interfaz gráfica.
- Ingresar el nombre del jugador humano.
- Seleccionar la cantidad de robots (de 1 a 3).
- Repartir siete cartas a cada participante al comenzar una ronda.
- Mostrar las cartas de la mano del jugador y la carta que se encuentra sobre la mesa.
- Validar las jugadas según el color o el valor de la carta superior y las condiciones de los comodines.
- Robar cartas cuando corresponde.
- Controlar el orden y el cambio de turnos.
- Aplicar los efectos de las cartas especiales: Salta, Reversa, +2, Cambio de color y +4.
- Seleccionar el color cuando se juega un comodín.
- Anunciar UNO cuando al jugador le queda una carta y aplicar la penalización de dos cartas cuando no lo anuncia a tiempo.
- Permitir que los robots seleccionen sus jugadas mediante estrategias automáticas.
- Determinar el ganador de cada ronda y calcular los puntos correspondientes.
- Acumular los puntos hasta que un participante alcance el límite de 250 puntos.
- Mostrar notificaciones y actualizar la interfaz cuando ocurren eventos durante la partida.
- Consultar el historial de resultados de las partidas.
2. Requisitos previos
- JDK 26 instalado.
- IntelliJ IDEA.
- Maven instalado si se desea compilar y ejecutar los comandos desde una terminal. IntelliJ IDEA también puede utilizar el Maven integrado.
El proyecto configura las versiones maven.compiler.source y maven.compiler.target en Java 26. Por eso, debe utilizarse un JDK compatible.
3. Descargar el proyecto
Clonar el repositorio:
git clone https://github.com/dsdavid0610/JuegoUnoSoftware.git
cd JuegoUnoSoftware
También se puede descargar el repositorio como archivo ZIP desde GitHub y descomprimirlo.
4. Abrir y ejecutar en IntelliJ IDEA
1. Abrir IntelliJ IDEA.
2. Seleccionar File → Open.
3. Elegir la carpeta raíz del proyecto, donde se encuentra el archivo pom.xml.
4. Esperar a que IntelliJ IDEA importe el proyecto y descargue las dependencias de Maven.
5. Ir a File → Project Structure → Project y seleccionar un JDK 26 como Project SDK.
6. En el panel Project, navegar hasta:
   src/main/java/edu/unilibre/controlador/VentanaUno.java
7. Abrir la clase VentanaUno.java.
8. Ejecutar el método main haciendo clic en el icono verde Run que aparece junto al método, o haciendo clic derecho sobre el archivo y seleccionando Run 'VentanaUno.main()'.
9. Al abrirse la ventana del programa, ingresar el nombre del jugador, seleccionar la cantidad de robots e iniciar la partida.
Clase principal: edu.unilibre.controlador.VentanaUno
5. Compilar y ejecutar desde la terminal
Abrir una terminal en la carpeta raíz del proyecto, en la misma ubicación donde está pom.xml.
Ejecutar las pruebas y comprobar la compilación
mvn clean test
Este comando limpia los archivos generados anteriormente, compila el proyecto y ejecuta las pruebas configuradas.
Generar el paquete compilado
mvn package
El resultado de la compilación se genera en la carpeta target/.

La aplicación gráfica se inicia ejecutando el método main de VentanaUno desde IntelliJ IDEA. El proyecto no configura en este README un JAR ejecutable con todas las dependencias; por ello, no se debe asumir que basta con ejecutar java -jar sobre cualquier archivo generado en target/.
6. Estructura general del proyecto
JuegoUnoSoftware/
├── pom.xml
└── src/
    ├── main/
    │   └── java/edu/unilibre/
    │       ├── builder/
    │       │   └── PartidaBuilder.java
    │       ├── controlador/
    │       │   └── VentanaUno.java
    │       ├── fabrica/
    │       │   ├── FabricaCartas.java
    │       │   ├── FabricaCartasEspeciales.java
    │       │   └── FabricaCartasNumericas.java
    │       ├── facade/
    │       │   ├── JuegoFacade.java
    │       │   └── ResultadoTurnoRobot.java
    │       ├── modelo/
    │       │   ├── Carta.java
    │       │   ├── ColorCarta.java
    │       │   ├── EstrategiaRobot.java
    │       │   ├── JuegoUno.java
    │       │   ├── Jugador.java
    │       │   ├── ObservadorJuego.java
    │       │   ├── RobotAgresivo.java
    │       │   ├── RobotEstandar.java
    │       │   └── TipoCarta.java
    │       └── vista/
    │           ├── AccionesTableroPanel.java
    │           ├── DibujoCartas.java
    │           ├── InicioMenuPanel.java
    │           ├── MenuPanel.java
    │           ├── ResultadoPanel.java
    │           └── TableroPanel.java
    └── test/
        └── java/edu/unilibre/
            ├── PartidaBuilderTest.java
            ├── VentanaUnoTest.java
            ├── FabricaCartasEspecialesTest.java
            ├── FabricaCartasNumericasTest.java
            ├── JuegoFacadeTest.java
            ├── CartaTest.java
            ├── JuegoUnoTest.java
            ├── JugadorTest.java
            ├── RobotAgresivoTest.java
            └── RobotEstandarTest.java
7. Patrones de diseño utilizados
El proyecto utiliza cinco patrones de diseño:
Strategy (Estrategia)
Permite definir distintas formas de seleccionar jugadas para los robots.
- EstrategiaRobot: interfaz que establece las operaciones para elegir una carta y un color.
- RobotEstandar: implementación de una estrategia de decisión estándar.
- RobotAgresivo: especializa el comportamiento y modifica la prioridad de ciertas cartas.
- Jugador: mantiene la estrategia asociada cuando representa a un robot.
Observer (Observador)
Permite notificar a la interfaz cuando ocurren eventos o cambios en el juego.
- ObservadorJuego: define el método de actualización.
- JuegoUno: mantiene los observadores y les comunica los eventos.
- VentanaUno: implementa el observador y actualiza la presentación gráfica al recibir notificaciones.
Factory Method (Método de fábrica)
Organiza la creación de cartas mediante fábricas especializadas.
- FabricaCartas: define la estructura de la fábrica.
- FabricaCartasNumericas: crea cartas numéricas.
- FabricaCartasEspeciales: crea cartas especiales.
Facade (Fachada)
Ofrece un punto de acceso simplificado a las operaciones principales del juego.
- JuegoFacade: coordina operaciones como iniciar la partida, jugar cartas, robar, decir UNO, gestionar el turno de los robots y consultar el historial.
- VentanaUno: utiliza la fachada para comunicarse con la lógica del juego.
Builder (Constructor)
Permite configurar y construir una partida paso a paso.
- PartidaBuilder: configura el nombre del jugador, la cantidad de robots y las fábricas de cartas.
- build(): construye y devuelve el objeto que representa la partida.
- JuegoFacade: utiliza el constructor al iniciar una nueva partida.
8. Pruebas
El proyecto contiene 10 pruebas unitarias organizadas bajo src/test/java. Para ejecutarlas desde la terminal:
mvn test
También se pueden ejecutar desde IntelliJ IDEA abriendo las clases de prueba y seleccionando Run.
9. Historial de partidas
La aplicación registra resultados en el archivo historial_uno.csv mediante la funcionalidad de historial de JuegoFacade. Este registro contiene información resumida de las partidas. No corresponde a guardar y restaurar el estado completo de una partida que todavía está en curso, este se puede borrar y no afectaría de nada el código, si se borra se vuelve a generar un archivo al finalizar la partida.
10. Información del proyecto
- Repositorio: https://github.com/dsdavid0610/JuegoUnoSoftware
- Wiki: https://github.com/dsdavid0610/JuegoUnoSoftware/wiki
- Lenguaje: Java
- Interfaz gráfica: Java Swing
- Gestor de dependencias y compilación: Maven
11. Integrantes
- David Santiago Granja Espinosa
- Juan Enrique Galeano Vargas
