package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.Formula;
import fr.cyr.marmite.app.service.dto.Formula;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formula} and its DTO {@link Formula}.
 */
@Mapper(componentModel = "spring")
public interface FormulaMapper extends EntityMapper<Formula, Formula> {}
