const DASHBOARD_CONFIG = {
    ADMINISTRADOR: {
        label: "Administrador",
        description: "Administra usuarios, perfiles y la configuración general de MediReservas.",
        actions: [
            { icon: "US", title: "Gestionar usuarios", description: "Crea, edita y cambia el estado de las cuentas.", href: "usuarios.html" },
            { icon: "RO", title: "Roles y permisos", description: "Asigna perfiles y revisa los accesos disponibles.", href: "roles.html" },
            { icon: "ME", title: "Gestionar médicos", description: "Mantén actualizada la información de los profesionales.", href: "admin-medicos.html" },
            { icon: "ES", title: "Especialidades", description: "Administra las áreas de atención médica disponibles.", href: "admin-especialidades.html" }
        ],
        summary: [
            { value: "4", label: "Perfiles del sistema" },
            { value: "12", label: "Usuarios activos" },
            { value: "8", label: "Especialidades" }
        ]
    },
    RECEPCIONISTA: {
        label: "Recepcionista",
        description: "Revisa y gestiona las solicitudes de atención de los pacientes.",
        actions: [
            { icon: "CI", title: "Gestionar citas", description: "Consulta todas las solicitudes y sus estados.", href: "gestion-citas.html" },
            { icon: "CO", title: "Confirmar citas", description: "Confirma las solicitudes que se encuentran pendientes.", href: "gestion-citas.html?estado=pendiente" },
            { icon: "RE", title: "Reagendar citas", description: "Modifica la fecha y hora de una atención solicitada.", href: "gestion-citas.html?accion=reagendar" }
        ],
        summary: [
            { value: "6", label: "Citas pendientes" },
            { value: "9", label: "Citas confirmadas" },
            { value: "3", label: "Cambios para hoy" }
        ]
    },
    MEDICO: {
        label: "Médico",
        description: "Consulta tu agenda y registra la información de tus atenciones.",
        actions: [
            { icon: "AG", title: "Mi agenda", description: "Revisa tus próximas citas ordenadas por fecha.", href: "agenda-medica.html" },
            { icon: "OB", title: "Registrar observación", description: "Añade observaciones después de atender a un paciente.", href: "observacion-clinica.html" },
            { icon: "HI", title: "Historial clínico", description: "Consulta antecedentes asociados a tus atenciones.", href: "historial-clinico.html" }
        ],
        summary: [
            { value: "5", label: "Atenciones de hoy" },
            { value: "2", label: "Observaciones pendientes" },
            { value: "18", label: "Atenciones semanales" }
        ]
    },
    PACIENTE: {
        label: "Paciente",
        description: "Reserva horas y consulta el estado de tus próximas atenciones médicas.",
        actions: [
            { icon: "RE", title: "Reservar una hora", description: "Selecciona especialidad, profesional, fecha y horario.", href: "solicitar-cita.html" },
            { icon: "MC", title: "Mis citas", description: "Consulta, revisa o cancela tus próximas atenciones.", href: "mis-citas.html" },
            { icon: "ME", title: "Buscar médicos", description: "Encuentra profesionales por nombre o especialidad.", href: "medicos.html" },
            { icon: "HI", title: "Historial clínico", description: "Revisa las observaciones de tus atenciones anteriores.", href: "historial-clinico.html" }
        ],
        summary: [
            { value: "2", label: "Próximas citas" },
            { value: "1", label: "Solicitud pendiente" },
            { value: "4", label: "Atenciones realizadas" }
        ]
    }
};

export function getDashboardConfig(role) {
    return DASHBOARD_CONFIG[role] ?? null;
}

export function isValidRole(role) {
    return Object.hasOwn(DASHBOARD_CONFIG, role);
}

export function validateRoleChange(currentRole, newRole) {
    if (!isValidRole(newRole)) {
        return "Selecciona un rol válido.";
    }

    if (currentRole === newRole) {
        return "Selecciona un rol diferente al actual.";
    }

    return "";
}
