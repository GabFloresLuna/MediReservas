import {getAppointments, getSession, getUserById, initializeBaseAppointments} from "./storage.js";

const consultationList = document.querySelector("#listaConsultas");
const emptyMessage = document.querySelector("#mensajeSinConsultas");

function appendDetail(container, label, value, className = "") {
    const paragraph = document.createElement("p");
    paragraph.className = className;

    const strong = document.createElement("strong");
    strong.textContent = `${label}: `;
    paragraph.append(strong, document.createTextNode(value));
    container.append(paragraph);
}

function formatDate(date) {
    return new Intl.DateTimeFormat("es-CL", {timeZone: "UTC"}).format(new Date(`${date}T00:00:00Z`));
}

function createConsultationCard(appointment, showPatient) {
    const card = document.createElement("article");
    card.className = "rounded-xl border border-line p-4";

    const title = document.createElement("h3");
    title.className = "font-semibold";
    title.textContent = `Consulta de ${appointment.specialtyName}`;
    card.append(title);

    if (showPatient) {
        appendDetail(card, "Paciente", `${appointment.patientName} (${appointment.patientRun})`, "mt-2");
    }
    appendDetail(card, "Fecha", formatDate(appointment.date), "mt-2");
    appendDetail(card, "Médico", appointment.doctorName);
    appendDetail(card, "Motivo", appointment.reason);
    appendDetail(card, "Diagnóstico", appointment.diagnosis);
    appendDetail(card, "Observación clínica", appointment.clinicalNotes);

    return card;
}

function renderHistory() {
    const session = getSession();
    const currentUser = getUserById(session?.userId);

    if (!currentUser) {
        emptyMessage.classList.remove("hidden");
        return;
    }

    const isDoctor = session.role === "MEDICO";
    document.querySelector("#patient-data-section").hidden = isDoctor;

    if (!isDoctor) {
        document.querySelector("#patient-name").textContent = `${currentUser.firstName} ${currentUser.lastName}`;
        document.querySelector("#patient-run").textContent = currentUser.run;
    }

    const consultations = getAppointments()
        .filter(
            (appointment) =>
                (isDoctor
                    ? appointment.doctorId === currentUser.id
                    : appointment.patientId === currentUser.id || appointment.patientRun === currentUser.run) &&
                appointment.status === "COMPLETADA" &&
                appointment.diagnosis &&
                appointment.clinicalNotes
        )
        .sort((first, second) => `${second.date} ${second.time}`.localeCompare(`${first.date} ${first.time}`));

    consultationList.replaceChildren(...consultations.map((appointment) => createConsultationCard(appointment, isDoctor)));
    emptyMessage.classList.toggle("hidden", consultations.length > 0);
}

initializeBaseAppointments();
renderHistory();
