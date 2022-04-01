import React, { useEffect, useState } from 'react';
import { Grid, Box } from '@mui/material';

import getProducts from 'src/api/service';
import { Product } from 'src/interfaces/Product';
import { ItemCard } from 'src/components/ItemCard';

export const ProductList: React.FC = () => {
  const [items, setItems] = useState<Product[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    async function get() {
      const prod = await getProducts();
      setItems(prod);
      setIsLoading(false);
    }
    get();
  }, []);

  if (isLoading) {
    return <p>Loading...</p>;
  }

  return (
    <Grid
      container
      alignItems="center"
      justifyContent="flex-start"
      spacing={{ xs: 2, md: 3 }}
      columns={{ xs: 4, sm: 8, md: 18 }}
    >
      {items.map(product => (
        <Grid item xs={2} sm={4} md={3} key={product.id}>
          <ItemCard item={product} />
        </Grid>
      ))}
    </Grid>
  );
};
