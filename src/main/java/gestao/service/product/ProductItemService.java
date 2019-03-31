package gestao.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gestao.model.product.ProductItem;
import gestao.model.product.ProductItemDto;
import gestao.repository.product.ProductItemRepository;

@Service
public class ProductItemService {

  @Autowired
  private ProductItemRepository productItemRepository;

  public Page<ProductItemDto> findAllHospitalProductItems(
    Long hospitalId,
    Pageable pageable
  ){
    return this.productItemRepository.findAllByHospitalId(hospitalId, pageable)
      .map(ProductItem::convertToDto);
  }
}