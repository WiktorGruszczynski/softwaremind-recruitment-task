export interface Product {
  id?: string;
  name: string;
  description: string;
  price: number;
  category: string;
}

export enum ProductCategory {
  ELECTRONICS = 'electronics',
  FASHION = 'fashion',
  HOME_GARDEN = 'home & garden',
  HEALTH = 'health',
  BEAUTY = 'beauty',
  SPORTS = 'sports',
  GROCERIES = 'groceries',
  AUTOMOTIVE = 'automotive',
  OTHER = 'other'
}