package gestao.controller.product;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestao.model.product.Product;
import gestao.service.product.ProductService;

/**
 * Controlador dos serviços relacionados ao produto.
 * 
 * @author edmilson.santana
 *
 */
@RestController
@RequestMapping("product")
public class ProductController {

	@Autowired
	private ProductService service;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Product create(@RequestBody @Valid Product product) {
		return this.service.create(product);
	}

	@GetMapping(params = {"page", "size"})
	public Iterable<Product> find(
		@RequestParam("page") int page,
		@RequestParam("size") int size
	) {
		return this.service.find(PageRequest.of(page, size));
	}

	@GetMapping("/{id}")
	public Product find(@PathVariable Long id) {
		return this.service.findById(id);
	}

	@PutMapping("/{id}")
	public Product update(@PathVariable Long id, @RequestBody @Valid Product product) {
		return this.service.update(id, product);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		this.service.delete(id);
	}

}
