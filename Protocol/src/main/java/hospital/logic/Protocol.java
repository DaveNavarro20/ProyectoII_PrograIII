package hospital.logic;

public class Protocol {
    public static final String SERVER = "DESKTOP-2JAD8JF";
    public static final int PORT = 1234;

    // Operaciones de Médicos
    public static final int MEDICO_CREATE = 101;
    public static final int MEDICO_READ = 102;
    public static final int MEDICO_UPDATE = 103;
    public static final int MEDICO_DELETE = 104;
    public static final int MEDICO_SEARCH = 105;

    // Operaciones de Farmacéuticos
    public static final int FARMACEUTICO_CREATE = 201;
    public static final int FARMACEUTICO_READ = 202;
    public static final int FARMACEUTICO_UPDATE = 203;
    public static final int FARMACEUTICO_DELETE = 204;
    public static final int FARMACEUTICO_SEARCH = 205;

    // Operaciones de Administradores
    public static final int ADMINISTRADOR_READ = 302;
    public static final int ADMINISTRADOR_UPDATE = 303;

    // Operaciones de Usuarios (Login y búsqueda)
    public static final int USUARIO_LOGIN = 401;
    public static final int USUARIO_BUSCAR = 402;
    public static final int USUARIO_ACTUALIZAR = 403;

    // Operaciones de Pacientes
    public static final int PACIENTE_CREATE = 501;
    public static final int PACIENTE_READ = 502;
    public static final int PACIENTE_UPDATE = 503;
    public static final int PACIENTE_DELETE = 504;
    public static final int PACIENTE_SEARCH = 505;

    // Operaciones de Medicamentos
    public static final int MEDICAMENTO_CREATE = 601;
    public static final int MEDICAMENTO_READ = 602;
    public static final int MEDICAMENTO_UPDATE = 603;
    public static final int MEDICAMENTO_DELETE = 604;
    public static final int MEDICAMENTO_SEARCH = 605;
    public static final int MEDICAMENTO_FINDALL = 606;

    // Operaciones de Recetas
    public static final int RECETA_CREATE = 701;
    public static final int RECETA_READ = 702;
    public static final int RECETA_UPDATE = 703;
    public static final int RECETA_DELETE = 704;
    public static final int RECETA_SEARCH = 705;
    public static final int RECETA_SEARCH_NO_ENTREGADAS = 706;
    public static final int RECETA_FIND_POR_ESTADO = 707;
    public static final int RECETA_FIND_NO_ENTREGADAS = 708;
    public static final int RECETA_MODIFICAR_ESTADO = 709;
    public static final int RECETA_FINDALL = 710;

    // **NUEVAS** Operaciones de Notificaciones y Usuarios Activos
    public static final int USUARIO_GET_ACTIVOS = 801;
    public static final int USUARIO_ENVIAR_MENSAJE = 802;
    public static final int USUARIO_GET_MENSAJES = 803; // Obtener mensajes pendientes

    // Errores y desconexión
    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_ERROR = 1;
    public static final int DISCONNECT = 99;
}