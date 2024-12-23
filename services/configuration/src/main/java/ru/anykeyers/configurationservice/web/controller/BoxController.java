package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.configurationservice.service.BoxService;
import ru.anykeyers.configurationservice.web.ControllerName;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.BOX_CONTROLLER)
@Tag(name = "Обработка боксов автомоек")
public class BoxController {

    private final ModelMapper modelMapper;

    private final BoxService boxService;

    @Operation(summary = "Получить все боксы автомойки")
    @GetMapping("/{carWashId}")
    public List<BoxDTO> getAllBoxes(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long carWashId
    ) {
        return boxService.getCarWashBoxes(carWashId).stream()
                .map(box -> modelMapper.map(box, BoxDTO.class))
                .toList();
    }

    @Operation(summary = "Добавить бокс автомойке")
    @PostMapping
    public void addBox(@AuthenticationPrincipal Jwt jwt, @RequestBody BoxDTO boxDTO) {
        boxService.addBox(JwtUtils.extractUser(jwt), boxDTO);
    }

    @Operation(summary = "Обновить бокс у автомойки")
    @PutMapping
    public void updateBox(@RequestBody BoxDTO boxDTO) {
        boxService.updateBox(boxDTO);
    }

    @Operation(summary = "Удалить бокс у автомойки")
    @DeleteMapping("/{boxId}")
    public void deleteBox(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long boxId
    ) {
        boxService.deleteBox(boxId);
    }

}
