package com.m9.crm.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> campos = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "inválido",
                        (a, b) -> a));
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "Dados inválidos", campos);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraint(ConstraintViolationException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RecursoNaoEncontradoException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(RegraDeNegocioException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuth(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Não autenticado", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccess(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Acesso negado", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", null);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String mensagem, Object detalhes) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensagem", mensagem);
        if (detalhes != null) body.put("detalhes", detalhes);
        return ResponseEntity.status(status).body(body);
    }
}
