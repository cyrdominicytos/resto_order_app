package fr.cyr.marmite.app.service.impl;

import fr.cyr.marmite.app.domain.Formula;
import fr.cyr.marmite.app.repository.FormulaRepository;
import fr.cyr.marmite.app.service.FormulaService;
import fr.cyr.marmite.app.service.dto.Formula;
import fr.cyr.marmite.app.service.mapper.FormulaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Formula}.
 */
@Service
@Transactional
public class FormulaServiceImpl implements FormulaService {

    private final Logger log = LoggerFactory.getLogger(FormulaServiceImpl.class);

    private final FormulaRepository formulaRepository;

    private final FormulaMapper formulaMapper;

    public FormulaServiceImpl(FormulaRepository formulaRepository, FormulaMapper formulaMapper) {
        this.formulaRepository = formulaRepository;
        this.formulaMapper = formulaMapper;
    }

    @Override
    public Formula save(Formula formula) {
        log.debug("Request to save Formula : {}", formula);
        Formula formula = formulaMapper.toEntity(formula);
        formula = formulaRepository.save(formula);
        return formulaMapper.toDto(formula);
    }

    @Override
    public Formula update(Formula formula) {
        log.debug("Request to update Formula : {}", formula);
        Formula formula = formulaMapper.toEntity(formula);
        formula = formulaRepository.save(formula);
        return formulaMapper.toDto(formula);
    }

    @Override
    public Optional<Formula> partialUpdate(Formula formula) {
        log.debug("Request to partially update Formula : {}", formula);

        return formulaRepository
            .findById(formula.getId())
            .map(existingFormula -> {
                formulaMapper.partialUpdate(existingFormula, formula);

                return existingFormula;
            })
            .map(formulaRepository::save)
            .map(formulaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Formula> findAll(Pageable pageable) {
        log.debug("Request to get all Formulas");
        return formulaRepository.findAll(pageable).map(formulaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Formula> findOne(Long id) {
        log.debug("Request to get Formula : {}", id);
        return formulaRepository.findById(id).map(formulaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formula : {}", id);
        formulaRepository.deleteById(id);
    }
}
