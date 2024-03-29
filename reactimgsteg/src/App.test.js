import { render, screen } from '@testing-library/react';
import App from './App';


//TODO - Add all the functional tests from java source code
test('renders dropzones', () => {
  render(<App />);
  const dropzoneImageText = screen.getByText(/Drop input image*/i);
  const dropzoneSeedText = screen.getByText(/Drop seed image*/i);
  const dropzoneSecretText = screen.getByText(/Drop input file*/i);

  expect(dropzoneImageText).toBeInTheDocument();
  expect(dropzoneSeedText).toBeInTheDocument();
  expect(dropzoneSecretText).toBeInTheDocument();
});
