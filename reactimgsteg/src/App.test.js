import { render, screen } from '@testing-library/react';
import App from './App';

test('renders dropzones', () => {
  render(<App />);
  const dropzoneInputText = screen.getByText(/Drop input*/i);
  const dropzoneSeedText = screen.getByText(/Drop seed image*/i);
  expect(dropzoneInputText).toBeInTheDocument();
  expect(dropzoneSeedText).toBeInTheDocument();
});
