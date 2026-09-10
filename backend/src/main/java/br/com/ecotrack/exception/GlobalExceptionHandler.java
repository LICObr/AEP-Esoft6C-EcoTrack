package br.com.ecotrack.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.ecotrack.domain.exception.ConflitoException;
import br.com.ecotrack.domain.exception.RecursoNaoEncontradoException;
import br.com.ecotrack.domain.exception.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException excecao, HttpServletRequest requisicao) {
        return construirResposta(HttpStatus.NOT_FOUND, excecao.getMessage(), requisicao);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<Map<String, Object>> tratarConflito(
            ConflitoException excecao, HttpServletRequest requisicao) {
        return construirResposta(HttpStatus.CONFLICT, excecao.getMessage(), requisicao);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, Object>> tratarRegraDeNegocio(
            RegraDeNegocioException excecao, HttpServletRequest requisicao) {
        return construirResposta(HttpStatus.UNPROCESSABLE_ENTITY, excecao.getMessage(), requisicao);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarArgumentoInvalido(
            IllegalArgumentException excecao, HttpServletRequest requisicao) {
        return construirResposta(HttpStatus.BAD_REQUEST, excecao.getMessage(), requisicao);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> tratarTipoInvalido(
            MethodArgumentTypeMismatchException excecao, HttpServletRequest requisicao) {
        String mensagem = "Valor invalido para o parametro '" + excecao.getName() + "'.";
        return construirResposta(HttpStatus.BAD_REQUEST, mensagem, requisicao);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException excecao, HttpServletRequest requisicao) {
        String mensagem = excecao.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Dados invalidos.");
        return construirResposta(HttpStatus.BAD_REQUEST, mensagem, requisicao);
    }

    private ResponseEntity<Map<String, Object>> construirResposta(
            HttpStatus status, String mensagem, HttpServletRequest requisicao) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        corpo.put("caminho", requisicao.getRequestURI());
        return ResponseEntity.status(status).body(corpo);
    }
}
