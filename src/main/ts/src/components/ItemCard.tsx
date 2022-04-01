import React, { useState } from 'react';
import {
  Card,
  Button,
  CardActions,
  CardContent,
  CardMedia,
  Typography,
  CardActionArea,
  Rating,
  Stack,
  Box,
} from '@mui/material';
import { makeStyles } from '@mui/styles';

import { Product } from 'src/interfaces/Product';

type Props = {
  item: Product;
};
const useStyles = makeStyles({
  multiLineEllipsis: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    display: '-webkit-box',
    '-webkit-line-clamp': 2,
    '-webkit-box-orient': 'vertical',
  },
});

export const ItemCard: React.FC<Props> = ({ item }) => {
  const classes = useStyles();

  return (
    <Card
      sx={{ minWidth: 275, width: '100%', borderRadius: '1px', margin: 'auto', border: 1, borderColor: 'grey.500' }}
    >
      <CardActionArea>
        <CardMedia sx={{ height: 257, width: '100%' }} component="img" image={item.imgLink} alt="my description" />
        <CardContent>
          <Typography className={classes.multiLineEllipsis} variant="body2" color="text.secondary">
            {item.name} {item.shortDescription}
          </Typography>
          <Stack sx={{ marginTop: 2 }} direction="row" justifyContent="space-between" alignItems="center" spacing={2}>
            <Rating style={{ color: '#FF7F50' }} name="read-only" value={5} readOnly size="small" />
            <h3>{item.price} $</h3>
          </Stack>
        </CardContent>
      </CardActionArea>
      <CardActions>
        <Button
          sx={{ borderRadius: '1px' }}
          style={{ backgroundColor: '#FF7F50', color: '#FFFFFF' }}
          fullWidth={true}
          variant="contained"
        >
          Add to Cart
        </Button>
      </CardActions>
    </Card>
  );
};
