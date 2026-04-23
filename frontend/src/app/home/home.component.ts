import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Product, ProductCategory } from "../features/products/models/product.model";
import { ProductService } from "../features/products/services/product.service";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  private productService = inject(ProductService);
  private router = inject(Router);

  expandedProductId: string | undefined;
  products: Product[] = [];
  allProducts: Product[] = []; 
  categories = Object.values(ProductCategory);

  ngOnInit() {
    this.loadProducts();
  }

  toggleDetails(productId: string | undefined) {
    this.expandedProductId = this.expandedProductId === productId ? undefined : productId;
  }

  loadProducts() {
    this.productService.getProducts().subscribe(data => this.products = data)
  }

  filterByCategory(category: string) {
    if (!category) {
      this.loadProducts();
      return;
    }
 
    this.productService.getProductsByCategory(category).subscribe(data => {
      this.products = data;
    });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  editProduct(product: Product) {

  }
}