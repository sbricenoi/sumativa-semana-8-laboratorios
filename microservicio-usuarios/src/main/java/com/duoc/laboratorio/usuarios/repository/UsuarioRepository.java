package com.duoc.laboratorio.usuarios.repository;

import com.duoc.laboratorio.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> findByEmail(@Param("email") String email);
    
    List<Usuario> findByRol(String rol);
    List<Usuario> findByActivo(Integer activo);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = :activo")
    List<Usuario> findByRolAndActivo(@Param("rol") String rol, @Param("activo") Integer activo);
    
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> buscarPorNombreOApellido(@Param("texto") String texto);
}
