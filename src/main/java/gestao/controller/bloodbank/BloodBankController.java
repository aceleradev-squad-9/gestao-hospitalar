package gestao.controller.bloodbank;

import gestao.model.bloodbank.BloodBank;
import gestao.service.bloodbank.BloodBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("bloodbank")
public class BloodBankController {

    @Autowired
    private BloodBankService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public BloodBank create(@RequestBody @Valid BloodBank bloodBank) {
        return this.service.create(bloodBank);
    }

    @GetMapping
    public Iterable<BloodBank> find() {
        return this.service.find();
    }

    @PutMapping
    public BloodBank update(@RequestBody @Valid BloodBank bloodBank) {
        return this.service.update(bloodBank);
    }

}
