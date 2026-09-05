import {
    getDoctors,
    getSpecialties,
    initializeBaseDoctors,
    initializeBaseSpecialties
} from "./storage.js";

const specialtySearch = document.querySelector("#buscarEspecialidad");
const specialtyList = document.querySelector("#listaEspecialidades");
const specialtyEmptyMessage = document.querySelector("#mensajeSinEspecialidades");
const doctorSearch = document.querySelector("#buscarMedico");
const specialtyFilter = document.querySelector("#filtroEspecialidad");
const doctorList = document.querySelector("#listaMedicos");
const doctorEmptyMessage = document.querySelector("#mensajeSinMedicos");
const doctorDetail = document.querySelector("#detalleMedico");

function getActiveSpecialties() {
    return getSpecialties().filter((specialty) => specialty.active);
}

function getDoctorSpecialties(doctor) {
    const doctorSpecialtyIds = [doctor.specialtyId, ...(doctor.extraSpecialtyIds ?? [])];
    return getActiveSpecialties().filter((specialty) => doctorSpecialtyIds.includes(specialty.id));
}

function createSpecialtyCard(specialty) {
    const card = document.createElement("article");
    const title = document.createElement("h3");
    const description = document.createElement("p");
    const button = document.createElement("button");

    card.className = "flex flex-col rounded-2xl border border-line bg-white p-6 shadow-sm transition hover:-translate-y-1 hover:border-primary hover:shadow-lg";
    title.className = "text-xl font-bold";
    title.textContent = specialty.specialtyName;
    description.className = "mt-3 flex-1 leading-7 text-muted";
    description.textContent = specialty.description;
    button.type = "button";
    button.dataset.specialtyId = specialty.id;
    button.className = "mt-5 self-start font-semibold text-primary-dark hover:text-primary";
    button.textContent = "Ver médicos";
    card.append(title, description, button);

    return card;
}

function renderSpecialties() {
    const search = specialtySearch.value.trim().toLowerCase();
    const specialties = getActiveSpecialties().filter((specialty) =>
        specialty.specialtyName.toLowerCase().includes(search)
    );

    specialtyList.replaceChildren(...specialties.map(createSpecialtyCard));
    specialtyEmptyMessage.classList.toggle("hidden", specialties.length > 0);
}

function fillSpecialtyFilter() {
    specialtyFilter.replaceChildren(new Option("Todas las especialidades", ""));
    getActiveSpecialties().forEach((specialty) => {
        specialtyFilter.add(new Option(specialty.specialtyName, specialty.id));
    });
}

function createDoctorCard(doctor) {
    const specialties = getDoctorSpecialties(doctor);
    const card = document.createElement("article");
    const title = document.createElement("h3");
    const specialty = document.createElement("p");
    const license = document.createElement("p");
    const button = document.createElement("button");

    card.className = "medico flex flex-col rounded-2xl border border-line bg-white p-6 shadow-sm";
    title.className = "text-xl font-bold";
    title.textContent = `${doctor.firstName} ${doctor.lastName}`;
    specialty.className = "mt-3 text-primary-dark";
    specialty.textContent = `Especialidad: ${specialties.map((item) => item.specialtyName).join(", ")}`;
    license.className = "mt-1 text-sm text-muted";
    license.textContent = `Registro médico: ${doctor.medicalLicenseNumber}`;
    button.type = "button";
    button.className = "verDetalle mt-5 self-start rounded-xl border border-primary px-4 py-2 font-semibold text-primary-dark transition hover:bg-primary-light";
    button.dataset.doctorId = doctor.doctorId;
    button.textContent = "Ver detalle";
    card.append(title, specialty, license, button);

    return card;
}

function renderDoctors() {
    const search = doctorSearch.value.trim().toLowerCase();
    const selectedSpecialtyId = Number(specialtyFilter.value);
    const doctors = getDoctors()
        .filter((doctor) => doctor.active)
        .filter((doctor) => `${doctor.firstName} ${doctor.lastName}`.toLowerCase().includes(search))
        .filter(
            (doctor) =>
                !selectedSpecialtyId ||
                doctor.specialtyId === selectedSpecialtyId ||
                (doctor.extraSpecialtyIds ?? []).includes(selectedSpecialtyId)
        );

    doctorList.replaceChildren(...doctors.map(createDoctorCard));
    doctorEmptyMessage.classList.toggle("hidden", doctors.length > 0);
}

function showDoctorDetail(doctorId) {
    const doctor = getDoctors().find((item) => item.doctorId === doctorId && item.active);
    if (!doctor) return;

    const specialties = getDoctorSpecialties(doctor);
    document.querySelector("#detalleNombre").textContent = `${doctor.firstName} ${doctor.lastName}`;
    document.querySelector("#detalleEspecialidad").textContent = specialties.map((item) => item.specialtyName).join(", ");
    document.querySelector("#detalleRegistro").textContent = doctor.medicalLicenseNumber;
    document.querySelector("#detalleDescripcion").textContent = specialties.map((item) => item.description).join(" ");
    doctorDetail.showModal();
}

specialtySearch?.addEventListener("input", renderSpecialties);
doctorSearch?.addEventListener("input", renderDoctors);
specialtyFilter?.addEventListener("change", renderDoctors);

specialtyList?.addEventListener("click", (event) => {
    const button = event.target.closest("button[data-specialty-id]");
    if (!button) return;

    specialtyFilter.value = button.dataset.specialtyId;
    renderDoctors();
    document.querySelector("#medicos").scrollIntoView({behavior: "smooth"});
});

doctorList?.addEventListener("click", (event) => {
    const button = event.target.closest("button[data-doctor-id]");
    if (button) showDoctorDetail(Number(button.dataset.doctorId));
});

document.querySelector("#cerrarDetalle")?.addEventListener("click", () => {
    doctorDetail.close();
});

doctorDetail?.addEventListener("click", (event) => {
    if (event.target === doctorDetail) doctorDetail.close();
});

initializeBaseSpecialties();
initializeBaseDoctors();
fillSpecialtyFilter();
renderSpecialties();
renderDoctors();
