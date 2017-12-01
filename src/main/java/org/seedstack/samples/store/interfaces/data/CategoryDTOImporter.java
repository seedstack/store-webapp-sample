package org.seedstack.samples.store.interfaces.data;

import java.util.stream.Stream;
import javax.inject.Inject;
import org.seedstack.business.assembler.dsl.FluentAssembler;
import org.seedstack.business.data.BaseDataImporter;
import org.seedstack.business.domain.Repository;
import org.seedstack.samples.store.domain.model.category.Category;
import org.seedstack.samples.store.domain.model.product.Product;

public class CategoryDTOImporter extends BaseDataImporter<CategoryDTO> {
    @Inject
    private Repository<Category, Long> categoryRepository;
    @Inject
    private Repository<Product, Long> productRepository;
    @Inject
    private FluentAssembler fluentAssembler;

    @Override
    public boolean isInitialized() {
        return !categoryRepository.isEmpty();
    }

    @Override
    public void clear() {
        categoryRepository.clear();
    }

    @Override
    public void importData(Stream<CategoryDTO> data) {
        data.forEach(categoryDTO -> {
            Category category = fluentAssembler.merge(categoryDTO).into(Category.class).fromFactory();
            categoryRepository.add(category);
            categoryDTO.getProducts().forEach(productDTO -> {
                Product product = fluentAssembler.merge(productDTO).into(Product.class).fromFactory();
                product.setCategoryId(category.getId());
                productRepository.add(product);
            });
        });
    }
}
