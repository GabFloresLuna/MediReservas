import {getAppointments, getSession, getUserById, initializeBaseAppointments} from "./storage.js";
import {formatAppointmentDate} from "./citas-utils.js";
import {appendLabeledText} from "./ui-utils.js";

const consultationList = document.querySelector("#listaConsultas");
const emptyMessage = document.querySelector("#mensajeSinConsultas");

function createConsultationCard(appointment, showPatient) {
    const card = document.createElement("article");
    card.className = "rounded-xl border border-line p-4";

    const title = document.createElement("h3");
    title.className = "font-semibold";
    title.textContent = `Consulta de ${appointment.specialtyName}`;
    card.append(title);

    if (showPatient) {
        appendLabeledText(card, "Paciente", `${appointment.patientName} (${appointment.patientRun})`, "mt-2");
    }
    appendLabeledText(card, "Fecha", formatAppointmentDate(appointment.date), "mt-2");
    appendLabeledText(card, "Médico", appointment.doctorName);
    appendLabeledText(card, "Motivo", appointment.reason);
    appendLabeledText(card, "Diagnóstico", appointment.diagnosis);
    appendLabeledText(card, "Observación clínica", appointment.clinicalNotes);

    return card;
}

function renderHistory() {
    const session = getSession();
    const currentUser = getUserById(session?.userId);

    if (!currentUser) {
        emptyMessage.classList.remove("hidden");
        return;
    }

    const isDoctor = session.role === "DOCTOR";
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
