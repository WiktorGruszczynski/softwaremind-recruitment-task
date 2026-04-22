import { Component, inject, OnInit } from '@angular/core';
import { Product } from "../features/products/models/product.model";
import { ProductService } from "../features/products/services/product.service";
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';



@Component({
  selector: 'app-home',
  standalone: true, 
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories = [
    'ELECTRONICS',
    'FASHION',
    'HOME_GARDEN',
    'HEALTH',
    'BEAUTY',
    'SPORTS',
    'GROCERIES',
    'AUTOMOTIVE',
    'OTHER'
  ];

  constructor(private productService: ProductService) {}
  
  private router = inject(Router);

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe(data => this.products = data);
  }


  filterByCategory(category: string) {
    if (!category) {
      this.loadProducts();
      return;
    }
    this.productService.getProductsByCategory(category).subscribe(data => this.products = data);
  }

  editProduct(product: Product) {
    this.productService.updateProduct(product.id!, product).subscribe(updated => {
      const index = this.products.findIndex(p => p.id === product.id);
      if (index !== -1) {
        this.products[index] = updated;
      }
    });
  }

  logout() {
      localStorage.removeItem('token');
      this.router.navigate(['/login']);
  }
}