package cl.duoc.auth.dto;

public record RoleResponseDTO(

		Long roleId,
		String roleName,
		String description,
		boolean active

) {
}